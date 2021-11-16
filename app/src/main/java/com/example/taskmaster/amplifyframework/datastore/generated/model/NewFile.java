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

/** This is an auto generated class representing the NewFile type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "NewFiles")
public final class NewFile implements Model {
  public static final QueryField ID = field("NewFile", "id");
  public static final QueryField FILE_NAME = field("NewFile", "fileName");
  public static final QueryField BELONGS_TO = field("NewFile", "newFileBelongsToId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String fileName;
  private final @ModelField(targetType="Task", isRequired = true) @BelongsTo(targetName = "newFileBelongsToId", type = Task.class) Task belongsTo;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getFileName() {
      return fileName;
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
  
  private NewFile(String id, String fileName, Task belongsTo) {
    this.id = id;
    this.fileName = fileName;
    this.belongsTo = belongsTo;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      NewFile newFile = (NewFile) obj;
      return ObjectsCompat.equals(getId(), newFile.getId()) &&
              ObjectsCompat.equals(getFileName(), newFile.getFileName()) &&
              ObjectsCompat.equals(getBelongsTo(), newFile.getBelongsTo()) &&
              ObjectsCompat.equals(getCreatedAt(), newFile.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), newFile.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getFileName())
      .append(getBelongsTo())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("NewFile {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("fileName=" + String.valueOf(getFileName()) + ", ")
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
  public static NewFile justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new NewFile(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      fileName,
      belongsTo);
  }
  public interface BelongsToStep {
    BuildStep belongsTo(Task belongsTo);
  }
  

  public interface BuildStep {
    NewFile build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep fileName(String fileName);
  }
  

  public static class Builder implements BelongsToStep, BuildStep {
    private String id;
    private Task belongsTo;
    private String fileName;
    @Override
     public NewFile build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new NewFile(
          id,
          fileName,
          belongsTo);
    }
    
    @Override
     public BuildStep belongsTo(Task belongsTo) {
        Objects.requireNonNull(belongsTo);
        this.belongsTo = belongsTo;
        return this;
    }
    
    @Override
     public BuildStep fileName(String fileName) {
        this.fileName = fileName;
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
    private CopyOfBuilder(String id, String fileName, Task belongsTo) {
      super.id(id);
      super.belongsTo(belongsTo)
        .fileName(fileName);
    }
    
    @Override
     public CopyOfBuilder belongsTo(Task belongsTo) {
      return (CopyOfBuilder) super.belongsTo(belongsTo);
    }
    
    @Override
     public CopyOfBuilder fileName(String fileName) {
      return (CopyOfBuilder) super.fileName(fileName);
    }
  }
  
}
