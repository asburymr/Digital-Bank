package io.digisic.credit.model.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.digisic.credit.model.UserProfile;
import io.digisic.credit.util.Messages;
import io.digisic.credit.util.Patterns;

@Entity
public class Users implements UserDetails {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(Users.class);
	
	private static final long serialVersionUID = -1173435728882792083L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
	@JsonProperty (access = Access.READ_ONLY)
    private Long id;
	
	@JsonProperty (access = Access.READ_ONLY)
    private String username;
    
	@NotEmpty(message=Messages.USER_PASSWORD_REQUIRED)
    @JsonProperty(access = Access.WRITE_ONLY, required = true)
    @Pattern(regexp=Patterns.USER_PASSWORD, message=Messages.USER_PASSWORD_FORMAT)
    private String password;

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="profile_id")
    private UserProfile userProfile;
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();
    
   
	public Users () {}
    
    public Users (String username, String password) {
    	this.username = username;
    	this.password = password;
    }
    
    @JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(ur -> authorities.add(new Authority(ur.getRole().getName())));
        
        return authorities;
	}
	

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
		
		return username;
	}

	public boolean isAccountNonExpired() {
		
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		
		return enabled;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param accountNonExpired the accountNotExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @param accountNonLocked the accountNotLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @param credentialsNonExpired the credentialNotExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

	/**
	 * @param userProfile the userProfile to set
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return the userRoles
	 */
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}


	public String toString() {
	    
		String user = "\n\nUser Profile ***********************";
	    
	    user += "\nId:\t\t\t" 					+ this.getId();
	    user += "\nUser Name:\t\t" 				+ this.getUsername();
	    user += "\nPassword:\t\t" 				+ this.getPassword();
	    user += "\nEnabled:\t\t" 				+ this.isEnabled();
	    user += "\nNon Locked:\t\t" 			+ this.isAccountNonLocked();
	    user += "\nNon Expired:\t\t" 			+ this.isAccountNonExpired();
	    user += "\nCredentials Non Expired:" 	+ this.isCredentialsNonExpired();
	    user += "\n" 							+ this.getUserProfile();

	    user += "\n*******************************************\n";
    
	    return user;
	}
	
}
