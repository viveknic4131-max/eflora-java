package bsi.eflora.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = { 
		@Index(name = "idx_users_email", columnList = "email"),
		@Index(name = "idx_users_deleted_at", columnList = "deleted_at") })
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, unique = true, length = 200)
	private String email;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(name = "email_verified_at")
	private LocalDateTime emailVerifiedAt;

	@Column(name = "fcm_token", columnDefinition = "TEXT")
	private String fcmToken;

	@Column(name = "email_notifications_enabled", nullable = false)
	private boolean emailNotificationsEnabled = true;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
