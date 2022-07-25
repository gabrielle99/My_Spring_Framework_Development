package cn.bugstack.springframework.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue){
        // append to the end of list -- which means there will be PropertyValue with same name.
        // this problem will be resolved when calling applyPropertyValues method in AbstractAutowireCapableBeanFactory
        // new values will cover the old value during the for loop
        propertyValueList.add(propertyValue);
    }

    public PropertyValue[] getPropertyValues(){
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName){
        for (PropertyValue pv : propertyValueList){
            if (pv.getName().equals(propertyName)){
                return pv;
            }
        }
        return null;
    }


}
