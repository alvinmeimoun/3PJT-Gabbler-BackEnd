
# Default users
INSERT INTO `gabbler`.`user` (`id`, `account_non_expired`, `account_non_locked`, `activation_code`, `birthdate`, `creation_date`, `credentials_non_expired`, `display_name`, `email`, `email_validated`, `enabled`, `firstname`, `gender`, `lastname`, `nickname`, `password`, `password_crypt_mode`, `phone`, `phone_indicator`) VALUES ('1', b'1', b'1', 'AEBF', '2015-01-01 00:00:00', '2015-01-05 00:00:00', b'1', 'Administrator', 'admin@gabbler.com', b'1', b'1', 'Admin', 'Male', 'Gabbler', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'SHA256', NULL, NULL);
INSERT INTO `gabbler`.`user` (`id`, `account_non_expired`, `account_non_locked`, `activation_code`, `birthdate`, `creation_date`, `credentials_non_expired`, `display_name`, `email`, `email_validated`, `enabled`, `firstname`, `gender`, `lastname`, `nickname`, `password`, `password_crypt_mode`, `phone`, `phone_indicator`) VALUES ('2', b'1', b'1', '12AF', '2015-01-02 00:00:00', '2015-01-05 07:07:12', b'1', 'Utilisateur de test', 'user@gabbler.com', b'1', b'1', 'Utilisateur', 'Male', 'Gabbler', 'user', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'SHA256', NULL, NULL);

# Role
INSERT INTO `gabbler`.`role` (`id`, `name`) VALUES ('1', 'ROLE_USER');
INSERT INTO `gabbler`.`role` (`id`, `name`) VALUES ('2', 'ROLE_ADMIN');

# Role<->User
INSERT INTO `gabbler`.`user_role` (`id_user`, `id_role`) VALUES ('1', '1'), ('1', '2'), ('2', '1');