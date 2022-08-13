# Spring Framework
Learning from spring framework, simulate an easy version of spring.

# Testing Result

**Test Code:**

![test_scope_code](./img/test_scope_code.png)

## Processor + Different Scope

**Processor:**

![beanPostProcessor_code](./img/beanPostProcessor_code.png)

![beanFactoryPostProcessor_code](./img/beanFactoryPostProcessor_code.png)



**Case 1**: scope="prototype"

![scope_xml](./img/prototype_xml.png)

Result when scope is prototype:

![prototype_result](./img/prototype_result.png)



**Case 2**: scope = "singleton" (by default)

![singleton_xml](./img/singleton_xml.png)

Result:

![singleton_result](./img/singleton_result.png)

(Only initiate once. During the econd time, will get the bean from cache.)



## Event:

![test_event_code](./img/test_event_code.png)

**Result:**

![event_result](./img/event_result.png)





