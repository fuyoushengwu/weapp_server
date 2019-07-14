INSERT INTO `user`(username,generic_score,password) 
SELECT * FROM (SELECT 'JENKINS_MANAGER_USERNAME',0,'$2a$10$79.gNAUlFzkIZ7twWEBJQ.L6sXJk90KlCH1tduxA/n6iU5nWLpeRC') AS tmp
WHERE NOT EXISTS (
	SELECT username FROM `user` WHERE username = 'JENKINS_MANAGER_USERNAME'
) LIMIT 1;

INSERT INTO `user_authority_list`(user_username,authority_list)
SELECT * FROM (SELECT 'JENKINS_MANAGER_USERNAME',1) AS tmp
WHERE NOT EXISTS (
	SELECT user_username FROM `user_authority_list` WHERE user_username = 'JENKINS_MANAGER_USERNAME'
) LIMIT 1;