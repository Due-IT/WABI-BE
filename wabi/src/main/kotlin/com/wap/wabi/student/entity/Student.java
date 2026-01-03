package com.wap.wabi.student.entity;

import com.wap.wabi.exception.ErrorCode;
import com.wap.wabi.exception.RestApiException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.jetbrains.annotations.NotNull;

@Entity
public class Student {
    @Id
    @NotNull
    private String id;
    private String name;
    private String email;

    private Student(builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
    }

    public static class builder {
        private String id;
        private String name;
        private String email;

        public builder id(@NotNull String id) {
            this.id = id;
            return this;
        }

        public builder name(String name) {
            this.name = name;
            return this;
        }

        public builder email(String email) {
            this.email = email;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    protected Student() {
    }

    @NotNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (id.isBlank()) {
            throw new RestApiException(ErrorCode.BAD_REQUEST_STUDENT_ID);
        }
    }
}
