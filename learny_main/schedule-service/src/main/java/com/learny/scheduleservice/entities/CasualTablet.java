package com.learny.scheduleservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "casual_tablets")
public class CasualTablet {
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ToString.Include
    @Column(name = "lesson_name")
    private String lessonName;

    @ToString.Include
    @Column(name = "group_number")
    private String groupNumber;

    @ToString.Include
    @Column(name = "office_number")
    private String officeNumber;

    @ToString.Include
    @Column(name = "createdAt")
    private Date createdAt = new Date();

    @ToString.Include
    @Column(name     = "startedAt")
    @DateTimeFormat(pattern = "HH:mm")
    private Date startedAt;

    @ToString.Include
    @Column(name = "endedAt")
    @DateTimeFormat(pattern = "HH:mm")
    private Date endedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CasualTablet that = (CasualTablet) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
