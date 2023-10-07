# MapRGenericRepo
Spring-boot and Apache MapR. 

## Usage
Like MongoDB driver and others.

```java
package io.github.honoriuss.mapr.repositories.entities;

public class YourEntity extends AEntity {
    public String identifier;
}
```

```java
import io.github.honoriuss.mapr.query.annotations.Repository;
import io.github.honoriuss.mapr.repositories.CRUDMapRRepository;

@Repository
public interface YourRepository extends CRUDMapRRepository<YourEntity> {
    YourEntity[] findByIdentifier(String identifier);
}
```
