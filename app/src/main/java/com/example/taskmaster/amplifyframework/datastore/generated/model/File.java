package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the File type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Files")
public final class File implements Model {
  public static final QueryField ID = field("File", "id");
  public static final QueryField NAME = field("File", "name");
  public static final QueryField BELONGS_TO = field("File", "fileBelongsToId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String name;
  private final @ModelField(targetType="Task", isRequired = true) @BelongsTo(targetName = "fileBelongsToId", type = Task.class) Task belongsTo;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public Task getBelongsTo() {
      return belongsTo;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private File(String id, String name, Task belongsTo) {
    this.id = id;
    this.name = name;
    this.belongsTo = belongsTo;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      File file = (File) obj;
      return ObjectsCompat.equals(getId(), file.getId()) &&
              ObjectsCompat.equals(getName(), file.getName()) &&
              ObjectsCompat.equals(getBelongsTo(), file.getBelongsTo()) &&
              ObjectsCompat.equals(getCreatedAt(), file.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), file.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getBelongsTo())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("File {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("belongsTo=" + String.valueOf(getBelongsTo()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BelongsToStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static File justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new File(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      belongsTo);
  }
  public interface BelongsToStep {
    BuildStep belongsTo(Task belongsTo);
  }
  

  public interface BuildStep {
    File build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep name(String name);
  }
  

  public static class Builder implements BelongsToStep, BuildStep {
    private String id;
    private Task belongsTo;
    private String name;
    @Override
     public File build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new File(
          id,
          name,
          belongsTo);
    }
    
    @Override
     public BuildStep belongsTo(Task belongsTo) {
        Objects.requireNonNull(belongsTo);
        this.belongsTo = belongsTo;
        return this;
    }
    
    @Override
     public BuildStep name(String name) {
        this.name = name;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, Task belongsTo) {
      super.id(id);
      super.belongsTo(belongsTo)
        .name(name);
    }
    
    @Override
     public CopyOfBuilder belongsTo(Task belongsTo) {
      return (CopyOfBuilder) super.belongsTo(belongsTo);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
  }
  
}
