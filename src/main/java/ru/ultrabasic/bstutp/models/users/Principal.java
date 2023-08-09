package ru.ultrabasic.bstutp.models.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Principal implements UserDetails {
    private User user;

    public Principal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().role));
        return authorities;
    }

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    public String getSurname() {
        return user.getSurname();
    }

    public String getPatronymic() {
        return user.getPatronymic();
    }

    public String getFullName() {
        String patronymic = getPatronymic();
        return getSurname() + " " + getName() + (patronymic == null || patronymic.isBlank() ? "" : " " + patronymic);
    }

    public String getFullNameShort() {
        String patronymic = getPatronymic();
        try {
            return getSurname() + " " + getName().charAt(0) + "." + (patronymic == null || patronymic.isBlank() ? "" : " " + patronymic.charAt(0) + ".");
        } catch (IndexOutOfBoundsException e) {
            return getFullName();
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
