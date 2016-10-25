-- Create syntax for TABLE 'article'
CREATE TABLE `article` (
  `id_article` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` text NOT NULL,
  `description` text NOT NULL,
  `create_at` date NOT NULL,
  `url` text,
  `image_url` text,
  PRIMARY KEY (`id_article`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create syntax for TABLE 'user'
CREATE TABLE `user` (
  `id_user` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` text,
  `login` text NOT NULL,
  `pass_hash` text NOT NULL,
  `pass_salt` text NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `login` (`login`(20))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create syntax for TABLE 'vendor'
CREATE TABLE `vendor` (
  `id_vendor` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `RSS_path` text NOT NULL,
  PRIMARY KEY (`id_vendor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;