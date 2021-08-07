package com.user.auth.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.user.auth.constants.RoleName;
import com.vladmihalcea.hibernate.type.array.ListArrayType;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "aws_roles")
@TypeDef(
	    name = "list-array",
	    typeClass = ListArrayType.class
	)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NonNull
    @NaturalId
    @Column(name = "role_name",length = 60)
    private RoleName roleName;
    

    @Type(type = "list-array")
    @Column(
            name = "allowed_functions",
            columnDefinition = "text[]"
        )
    private List<String> allowedFunctions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

	public List<String> getAllowedFunctions() {
		return allowedFunctions;
	}

	public void setAllowedFunctions(List<String> allowedFunctions) {
		this.allowedFunctions = allowedFunctions;
	}

	
}