package com.sap.tip.production.xcode.jaxb;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.sap.tip.production.xcode.Array;
import com.sap.tip.production.xcode.Dict;

public class JAXBDictAdapter extends XmlAdapter<JAXBDict, Dict>
{
  @Override
  public JAXBDict marshal(Dict dict) throws Exception
  {
    JAXBDict jaxbDict = new JAXBDict();
    Array elements = new JAXBPlist().createArray();
    for (Map.Entry<String, Object> entry : dict.entrySet())
    {
      JAXBKey key = new JAXBKey();
      key.setValue(entry.getKey());
      elements.add(key);

      Object value = entry.getValue();
      if (value instanceof Dict)
      {
        value = marshal((Dict) value);
      }
      else if (value instanceof Array)
      {
        value = new JAXBArrayAdapter().marshal((Array) value);
      }
      elements.add(value);
    }
    jaxbDict.setElements(elements);
    return jaxbDict;
  }

  @Override
  public Dict unmarshal(JAXBDict jaxbDict) throws Exception
  {
    Dict dict = new LinkedHashMapDict();
    for (int i = 0; i < jaxbDict.getElements().size(); i += 2)
    {
      Object key = jaxbDict.getElements().get(i);
      if (key instanceof JAXBKey)
      {
        key = ((JAXBKey) key).getValue();
      }

      Object value = jaxbDict.getElements().get(i + 1);
      if (value instanceof JAXBDict)
      {
        value = unmarshal((JAXBDict) value);
      }
      else if (value instanceof JAXBArray)
      {
        value = new JAXBArrayAdapter().unmarshal((JAXBArray) value);
      }
      dict.put((String) key, value);
    }
    return dict;
  }
}