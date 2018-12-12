

DROP TABLE user;
DROP TABLE tweet;

CREATE TABLE author_data (

    author_id LONGTEXT PRIMARY KEY NOT NULL,
    description LONGTEXT NOT NULL,
    location LONGTEXT NOT NULL,
    followers_count LONGTEXT NOT NULL,
    friends_count LONGTEXT NOT NULL,
);


CREATE TABLE tweet_data (
	tweet_id LONGTEXT PRIMARY KEY NOT NULL,
	tweet_author_id LONGTEXT NOT NULL,
	created_at LONGTEXT,
	retweeted_count LONGTEXT NOT NULL,
	tweet_text LONGTEXT NOT NULL,

	FOREIGN KEY (tweet_author_id) REFERENCES user(author_id)

);