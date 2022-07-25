# Testing Result

**Test Code:**

![test_scope_code](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/test_scope_code.png)

## Processor + Different Scope

**Processor:**

![beanPostProcessor_code](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/beanPostProcessor_code.png)

![beanFactoryPostProcessor_code](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/beanFactoryPostProcessor_code.png)



**Case 1**: scope="prototype"

![scope_xml](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/prototype_xml.png)

Result when scope is prototype:

![prototype_result](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/prototype_result.png)



**Case 2**: scope = "singleton" (by default)

![singleton_xml](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/singleton_xml.png)

Result:

![singleton_result](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/singleton_result.png)

(Only initiate once. During the econd time, will get the bean from cache.)



## Event:

![test_event_code](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/test_event_code.png)

**Result:**

![event_result](/Users/tianci/Downloads/small-spring-main/small-spring-learn/img/event_result.png)





