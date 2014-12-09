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

1. start with `com.github.dreamhead.moco.parser.model.SessionSetting`, create a json runner/related runner
2. run it
3.



### use in test

```
running {
  //interact with server and expect result
}
```

======HERE========
### support config
    - config file root DONE
    - config context

### separating unit tests and functional tests
