# MapRGenericRepo
Spring-boot and Apache MapR. 

## Usage
Like MongoDB driver and others.

```java
import io.github.honoriuss.mapr.generator.annotations.Entity;
import io.github.honoriuss.mapr.repositories.entities.AEntity;

@Entity
public class YourEntity extends AEntity {
    public String identifier;
}
```

```java
import io.github.honoriuss.mapr.generator.annotations.Repository;
import io.github.honoriuss.mapr.generator.interfaces.IMapRRepository;

@Repository(tablePath = "your/path")
public interface YourRepository extends IMapRRepository<YourEntity> {
    YourEntity[] findByIdentifier(String identifier);
}
```
