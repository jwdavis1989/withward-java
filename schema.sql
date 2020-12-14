drop table if exists users, withlists, destinations, ratings, withlistusers;

drop table if exists users;
create table users (
	user_id 					SERIAL primary key,
	username 					VARCHAR(100),
	user_email 					VARCHAR(100),
	user_salt					BYTEA,
	user_password 				VARCHAR(255),
	is_admin					BOOLEAN default false,
	user_photo 					VARCHAR(100)
);


drop table if exists withlists;
create table withlists (
	withlist_id 				SERIAL primary key,
	owner_id 					INT,
	withlist_title 				VARCHAR(100),
	withlist_description 		VARCHAR(100),
	constraint fk_user
		foreign KEY(owner_id)
			references users(user_id)
			on delete cascade
);

--select* from withlists;

drop table if exists destinations;
create table destinations (
	dest_id 					SERIAL primary key,
	withlist_id 				INT,
	dest_name 					VARCHAR(100),
	dest_description 			VARCHAR(100),
	dest_photo 					VARCHAR(100),
	dest_completed				BOOLEAN default false,
	dest_averageRating 			NUMERIC(3,2),
	constraint fk_withlist
		foreign KEY(withlist_id)
			references withlists(withlist_id)
			on delete cascade
);

drop table if exists withlistusers;
create table withlistusers (
	member_id					SERIAL primary key,
	user_id 					INT,
	withlist_id 				INT,
	constraint fk_withlist
	foreign KEY(withlist_id)
		references withlists(withlist_id)
		on delete cascade,
	constraint fk_user
	foreign KEY(user_id)
		references users(user_id)
		on delete cascade
);
--select * from destinations;

drop table if exists ratings;
create table ratings (
	rating_id 					SERIAL primary key,
	destination_id 				INT,
	user_id 					INT,
	rating_value 				NUMERIC(3,2),
	constraint fk_destination
	foreign KEY(destination_id)
		references destinations(dest_id)
		on delete cascade,
	constraint fk_user
	foreign KEY(user_id)
		references withlistusers(member_id)
		on delete cascade
);


insert into users (username, user_email, user_password, user_photo)
VALUES ('user1', 'user1@email.com', '2222', 'photoplaceholder'),
('user2', 'user2@email.com', '1111', 'photoplaceholder'),
('user3', 'user3@email.com', '3334', 'photoplaceholder');

insert into withlists (owner_id, withlist_title, withlist_description)
VALUES (1, 'Swimming', 'Got to get all those swims in.'),
(2, 'Vacation', 'Vacation list to track vacation destinations.'),
(1, 'Roadtrip 2020', 'Roadtrip list to track roadtrip destinations.');

insert into destinations (withlist_id, dest_name, dest_description, dest_photo)
VALUES (3, 'Trailhead1', 'hike to the midpoint', 'placeholder'),
(1, 'Northshore', 'hike to the beach', 'placeholder'),
(2, 'Hawaii', '2021 Christmas', 'placeholder'),
(2, 'Colorado', 'home to visit parents', 'placeholder');

insert into withlistusers (withlist_id, user_id)
VALUES 
(1,1),(1,2),(2,2), (3,3);

insert into ratings (destination_id, user_id, rating_value)
VALUES 
(2,1,4.00);