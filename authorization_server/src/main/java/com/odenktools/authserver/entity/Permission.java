package com.odenktools.authserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@JsonSerialize
@Entity(name = "Permission")
@Table(name = "permissions",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"name_permission", "readable_name"})})
public class Permission implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_perm", nullable = false)
  private Long idPerm;

  /**
   * Alias untuk kolum ``readableName``. ini digunakan agar data tetap konstant, tidak berpengaruh oleh update.
   * Ini harus digenerate ``UNIQUE`` berdasarkan kolum ``readableName``.
   * Misalkan :
   * named = Write ApiKey
   * coded = ROLE_WRITE_APIKEY (UPPERCASE, hapus SPACE menjadi UNDERSCORES, Tambahkan ROLE_)
   * </p>
   */
  @NotNull
  @Column(name = "name_permission", nullable = false)
  private String namePermission;

  @NotNull
  @Column(name = "readable_name", nullable = false)
  private String readableName;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", updatable = false)
  @JsonProperty("created_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  @Column(name = "updated_at")
  private Date updatedAt;

  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  @Column(name = "deleted_at")
  private Date deletedAt;

  public Permission() {

  }

  public Permission(@NotNull @Size(max = 100) String namePermission,
                    @NotNull @Size(max = 100) String readableName) {

    this.namePermission = namePermission;
    this.readableName = readableName;
  }

  /**
   * Sets created_at before insert.
   */
  @PrePersist
  public void setCreationDate() {

    this.createdAt = new Date();
  }

  /**
   * Sets updated_at before update.
   */
  @PreUpdate
  public void setChangedDate() {

    this.updatedAt = new Date();
  }

  @ManyToMany(mappedBy = "usersPermissions")
  private Set<Group> usersGroups;
}
