### target

###record

##### one request
```
theServer when {
    method "get"
      uri "/user"
  } then {
    "user detail"
  }
```

##### multiple requests

```
theServer when {
    method "get"
      uri "/user"
  } then {
    "user detail"
  } when {
      method "post"
    }
  then {
    "body"
  }
```


### use in test

```
running {
  //interact with server and expect result
}
```

======HERE========
### support config
    - config file root DONE
    - config context DONE

### separating unit tests and functional tests DONE

### support cookie
