drop database if exists nosh_map_test;
create database nosh_map_test;
use nosh_map_test;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default(1)
);

create table restaurant (
    restaurant_id int primary key auto_increment,
    google_place_Id varchar(200),
    restaurant_name varchar(255) not null,
    address varchar(255) not null,
    zipcode varchar(10) not null,
    phone varchar(15),
    website varchar(255),
    email varchar(255),
    latitude varchar(25) not null,
    longitude varchar(25) not null,
    state varchar(2) not null,
    city varchar(50) not null,
    hours_interval varchar(255),
    cuisine_type varchar(255)
);

create table user_profile (
	app_user_id int not null,
	first_name varchar(20) not null,
	last_name varchar(20) not null,
	address varchar(50),
    constraint fk_user_profile_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id)
);

create table meal (
	meal_id int primary key auto_increment,
    title varchar(100) not null,
    price decimal(5,2) not null,
    image_url varchar(255) not null,
    `description` varchar(500),
    last_updated datetime not null,
    is_public boolean not null,
    app_user_id int not null,
    restaurant_id int not null,
    constraint fk_meal_app_user_id
		foreign key (app_user_id)
        references app_user(app_user_id),
	constraint fk_meal_restaurant_id
        foreign key (restaurant_id)
        references restaurant(restaurant_id)
);

create table user_comment (
	user_comment_id int primary key auto_increment,
    `description` varchar(500),
    is_public boolean not null,
    post_date datetime not null,
    app_user_id int not null,
    restaurant_id int not null,
    constraint fk_user_comment_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_user_comment_restaurant_id
        foreign key (restaurant_id)
        references restaurant(restaurant_id)
);

create table rating (
	rating_id int primary key auto_increment,
    score decimal(2, 1) not null,
    `description` varchar(250),  
    app_user_id int not null,
    restaurant_id int not null,
    constraint fk_rating_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_rating_restaurant_id
        foreign key (restaurant_id)
        references restaurant(restaurant_id)
);

create table app_user_meal (
	app_user_id int not null,
    meal_id int not null,
    identifier varchar(50) not null,
    constraint pk_app_user_meal
        primary key (app_user_id, meal_id),
    constraint fk_app_user_meal_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_meal_meal_id
        foreign key (meal_id)
        references meal(meal_id),
    constraint uq_app_user_meal_identifier_app_user_id
        unique (identifier, app_user_id)
);

create table app_user_restaurant (
	app_user_id int not null,
    restaurant_id int not null,
    identifier varchar(50),
    constraint pk_app_user_restaurant
        primary key (app_user_id, restaurant_id),
    constraint fk_app_user_restaurant_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_restaurant_restaurant_id
        foreign key (restaurant_id)
        references restaurant(restaurant_id),
    constraint uq_app_user_restaurant_identifier_app_user_id
        unique (identifier, app_user_id)
);

delimiter //
create procedure set_known_good_state()
begin
	-- reset tables
    delete from app_user_restaurant;
    alter table app_user_restaurant auto_increment = 1;
    delete from app_user_meal;
    alter table app_user_meal auto_increment = 1;
    delete from rating;
    alter table rating auto_increment = 1;
    delete from user_comment;
    alter table user_comment auto_increment = 1;
    delete from meal;
    alter table meal auto_increment = 1;
    delete from user_profile;
    alter table user_profile auto_increment = 1;
    delete from restaurant;
    alter table restaurant auto_increment = 1;
    delete from app_user;
    alter table app_user auto_increment = 1;
    
	insert into app_user (username, password_hash, enabled)
		values
	('the@oracle.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
	('white@rabbit.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1);
    
	insert into restaurant
		(google_place_id, restaurant_name, address, zipCode, phone, website, email, latitude, longitude, state, city, hours_interval, cuisine_type)
		values 
		('testGoogleId', 'Vivi Bubble Tea', '1324 2nd Ave', "10021-5408", '+1 646-858-0710', 'http://www.vivibubbletea.com/', null, 40.76726, -73.95927,'NY','New York City', 'Sun - Sat (11:00 AM - 9:00 PM)', 'Healthy'),
		('testGoogleId', 'Little Italy Pizza', '333 5th Ave', "10016",'+1 212-481-5200','http://www.littleitalypizza.com', null, 40.747684, -73.9849,'NY','New York City', null, 'Italian,Fast Food,Pizza'),
		('testGoogleId', 'Locanda Verde', '377 Greenwich St', '10013-2338', '+1 212-925-3797', 'http://locandaverdenyc.com/', 'info@locandaverdenyc.com', 40.720085, -74.01022, 'NY', 'New York City',
			'Mon (5:00 PM - 10:45 PM) | Tue - Sun (5:00 PM - 11:00 PM) | Tue - Fri (7:00 AM - 11:00 AM) | Sat - Mon (7:00 AM - 3:00 PM)', 'American,Italian'),
		('testGoogleId', 'Ippudo', '65 4th Ave', "10003", '+1 212-388-0088', 'https://ippudony.com/', 'contactus@ippudo-us.com', 40.73098, -73.99029, 'NY', 'New York City', '11:30AM-10:30PM', 'Japanese,Ramen'),
		('testGoogleId', "Sylvia's Restaurant", '328 Malcolm X Blvd', "10027", '+1 212-996-0660', 'https://sylviasrestaurant.com/', null, 40.80875, -73.94467, 'NY', 'New York City', 
			'Wed - Sat (11:00 AM - 10:00 PM) | Sun - Tues (11:00 AM - 8:00 PM)', 'Soul Food'),
		('testgoogleId', 'Cho Dang Go', '55 W 35th St', '10001', '', 'https://chodanggolnyc.com/', 'chodanggol@handhospitality.com', 40.75017, -73.98635, 'NY', 'New York City', 
			'Sun - Sat (12:00 PM - 2:15 PM, 5:00 PM - 9:15 PM)', 'Korean');

	insert into user_profile (app_user_id, first_name, last_name, address)
		values 
		(1, "Gloria", "Foster", "Unknown"),
		(2, "Ada", "Nicodemou","String for now");

	insert into meal (title, price, image_url, `description`, last_updated, is_public, app_user_id, restaurant_id)
		values
		("Taro Creme Brulee", 7.75, "https://vivigaithersburg.square.site/uploads/1/3/1/4/131496331/s411215047120057019_p15_i2_w1795.png?width=1200&optimize=medium", 
			"Sweet potato bubble milk tea with creme.", "2023-06-25 14:27:00", true, 2, 1),
		("Thai Milk Tea", 5.75, "https://viviaurora.square.site/uploads/1/3/1/7/131791451/s687728388830480539_p7_i1_w2090.png?width=1200&optimize=medium",
			"Thai bubble milk tea", "2023-05-25 14:27:00", true, 2, 1),
		("White Pizza", 28.99, "https://slice-menu-assets-prod.imgix.net/3650/1607714310_aa1feb36e3?fit=crop&w=1500&h=937",
			"Cheese pizza with mozz and ricotta cheese. It slaps.", "2023-02-22 14:27:00", true, 1, 2),
		("Karaka Spicy", 24.00, "https://ippudo-us.com/wordpress/wp-content/themes/ippudo-theme/img/photo_karaka_3.png", 
		"Karaka tantan tonkotsu ramen noodle soup. Pretty spicy!", "2023-07-2 14:27:00",true, 2, 4),
		("Akamaru Modern", 22.00, "https://ippudo-us.com/wordpress/wp-content/themes/ippudo-theme/images/menu/photo_akamaru.png", 
			"miso paste, pork belly chashu, cabbage, sesame kikurage mushrooms and a whole lot of goodness", "2023-07-2 14:27:00", true, 2, 4),
		("Sylvia's Down Home Fried", 1.00, "https://pbs.twimg.com/media/D4dR-LjXkAAPlDg?format=png&name=small", 
			"Fried Chicken with String Beans and Baked Macaroni & Cheese", "2023-07-2 14:27:00", true, 1, 5),
		('Boneless Korean Fried Chicken', 16.99, 
			'https://images.squarespace-cdn.com/content/v1/59552931b8a79b0159375df0/1671805295809-I8E5MWV0QG5LOMEY2XPO/CDG+boneless+korean+fried+chicken+rgb.jpg?format=2500w', 
			'Korean style boneless fried chicken served with homemade sweet chili sauce', '2023-07-2 16:27:30', true, 1, 6),
		('Beef & Mushroom Japchae', 19.99, 'https://images.squarespace-cdn.com/content/v1/59552931b8a79b0159375df0/1671805471021-09PKWZ420ZD6VYH8ZOLT/CDG+beef+%26+mushroom+japchae+rgb.jpg?format=2500w', 
			'Stir-fried glass noodles with marinated beef, onions, peppers, spinach & assorted mushrooms', '2023-07-4 16:27:30', true, 1, 6),
		('Spicy Seafood Tofu Stew', 15.99, 'https://images.squarespace-cdn.com/content/v1/59552931b8a79b0159375df0/1671809220909-JUF9O6JQUJ7L57UNVFHG/CDG+spicy+seafood+tofu+stew+2+rgb.jpg?format=2500w', 
			'Spicy stew with CDG homemade tofu, soft silky tofu, and assorted seafood', '2023-07-1 13:17:30', true, 1, 6);
	
	insert into user_comment (`description`, is_public, post_date, app_user_id, restaurant_id)
		values
		("I'll be back...", true, '2023-06-25 14:27:00', 2, 1),
		("It was okay.", true, '2023-05-25 14:27:00', 2, 1),
		("Getchu some.", true, '2023-02-22 14:27:00', 1, 2);

	insert into rating (score, `description`, app_user_id, restaurant_id)
		values
		(5, 'Addictive', 2, 1),
		(3, 'Not for me', 2, 1),
		(5, 'My go-to', 1, 2);
        
	insert into app_user_restaurant (app_user_id, restaurant_id, identifier)
		values
		(1, 2, '1-2'),
		(2, 1, '2-1'),
		(2, 3, '2-3'),
		(2, 4, '2-4'),
		(1, 5, '1-5');
		
end //

insert into app_user (username, password_hash, enabled)
	values
	('the@oracle.com', '$2y$10$gFQx2WBVv0.mIsH.TmJ7o.LXLMjUTKSHrVQtrhg9tybjI6XrTgVXm', 1),
	('white@rabbit.com', '$2y$10$gFQx2WBVv0.mIsH.TmJ7o.LXLMjUTKSHrVQtrhg9tybjI6XrTgVXm', 1);
    
insert into restaurant
	(google_place_id, restaurant_name, address, zipCode, phone, website, email, latitude, longitude, state, city, hours_interval, cuisine_type)
	values 
	('testGoogleId', 'Vivi Bubble Tea', '1324 2nd Ave', "10021-5408", '+1 646-858-0710', 'http://www.vivibubbletea.com/', null, 40.76726, -73.95927,'NY','New York City', 'Sun - Sat (11:00 AM - 9:00 PM)', 'Healthy'),
	('testGoogleId', 'Little Italy Pizza', '333 5th Ave', "10016",'+1 212-481-5200','http://www.littleitalypizza.com', null, 40.747684, -73.9849,'NY','New York City', null, 'Italian,Fast Food,Pizza'),
    ('testGoogleId', 'Locanda Verde', '377 Greenwich St', '10013-2338', '+1 212-925-3797', 'http://locandaverdenyc.com/', 'info@locandaverdenyc.com', 40.720085, -74.01022, 'NY', 'New York City',
        'Mon (5:00 PM - 10:45 PM) | Tue - Sun (5:00 PM - 11:00 PM) | Tue - Fri (7:00 AM - 11:00 AM) | Sat - Mon (7:00 AM - 3:00 PM)', 'American,Italian'),
	('testGoogleId', 'Ippudo', '65 4th Ave', "10003", '+1 212-388-0088', 'https://ippudony.com/', 'contactus@ippudo-us.com', 40.73098, -73.99029, 'NY', 'New York City', '11:30AM-10:30PM', 'Ramen'),
	('testGoogleId', "Sylvia's Restaurant", '328 Malcolm X Blvd', "10027", '+1 212-996-0660', 'https://sylviasrestaurant.com/', null, 40.80875, -73.94467, 'NY', 'New York City', 
		'Wed - Sat (11:00 AM - 10:00 PM) | Sun - Tues (11:00 AM - 8:00 PM)', 'Soul Food'),
	('googleId', 'Cho Dang Go', '55 W 35th St', '10001', '', 'https://chodanggolnyc.com/', 'chodanggol@handhospitality.com', 40.75017, -73.98635, 'NY', 'New York City', 
		'Sun - Sat (12:00 PM - 2:15 PM, 5:00 PM - 9:15 PM)', 'Korean');

insert into user_profile (app_user_id, first_name, last_name, address)
	values 
	(1, "Gloria", "Foster", "Unknown"),
	(2, "Ada", "Nicodemou","String for now");

insert into meal (title, price, image_url, `description`, last_updated, is_public, app_user_id, restaurant_id)
	values
	("Taro Creme Brulee", 7.75, "https://vivigaithersburg.square.site/uploads/1/3/1/4/131496331/s411215047120057019_p15_i2_w1795.png?width=1200&optimize=medium", 
    "Sweet potato bubble milk tea with creme.", "2023-06-25 14:27:00", true, 2, 1),
	("Thai Milk Tea", 5.75, "https://viviaurora.square.site/uploads/1/3/1/7/131791451/s687728388830480539_p7_i1_w2090.png?width=1200&optimize=medium",
    "Thai bubble milk tea", "2023-05-25 14:27:00", true, 2, 1),
	("White Pizza", 28.99, "https://slice-menu-assets-prod.imgix.net/3650/1607714310_aa1feb36e3?fit=crop&w=1500&h=937",
    "Cheese pizza with mozz and ricotta cheese. It slaps.", "2023-02-22 14:27:00", true, 1, 2),
	("Karaka Spicy", 24.00, "https://ippudo-us.com/wordpress/wp-content/themes/ippudo-theme/img/photo_karaka_3.png", 
		"Karaka tantan tonkotsu ramen noodle soup. Pretty spicy!", "2023-07-2 14:27:00",true, 2, 4),
    ("Akamaru Modern", 22.00, "https://ippudo-us.com/wordpress/wp-content/themes/ippudo-theme/images/menu/photo_akamaru.png", 
		"miso paste, pork belly chashu, cabbage, sesame kikurage mushrooms and a whole lot of goodness", "2023-07-2 14:27:00", true, 2, 4),
	("Sylvia's Down Home Fried", 1.00, "https://pbs.twimg.com/media/D4dR-LjXkAAPlDg?format=png&name=small", 
		"Fried Chicken with String Beans and Baked Macaroni & Cheese", "2023-07-2 14:27:00", true, 1, 5),
	('Boneless Korean Fried Chicken', 16.99, 
		'https://images.squarespace-cdn.com/content/v1/59552931b8a79b0159375df0/1671805295809-I8E5MWV0QG5LOMEY2XPO/CDG+boneless+korean+fried+chicken+rgb.jpg?format=2500w', 
		'Korean style boneless fried chicken served with homemade sweet chili sauce', '2023-07-2 16:27:30', true, 1, 6),
    ('Beef & Mushroom Japchae', 19.99, 'https://images.squarespace-cdn.com/content/v1/59552931b8a79b0159375df0/1671805471021-09PKWZ420ZD6VYH8ZOLT/CDG+beef+%26+mushroom+japchae+rgb.jpg?format=2500w', 
		'Stir-fried glass noodles with marinated beef, onions, peppers, spinach & assorted mushrooms', '2023-07-4 16:27:30', true, 1, 6),
    ('Spicy Seafood Tofu Stew', 15.99, 'https://images.squarespace-cdn.com/content/v1/59552931b8a79b0159375df0/1671809220909-JUF9O6JQUJ7L57UNVFHG/CDG+spicy+seafood+tofu+stew+2+rgb.jpg?format=2500w', 
		'Spicy stew with CDG homemade tofu, soft silky tofu, and assorted seafood', '2023-07-1 13:17:30', true, 1, 6);
	
insert into user_comment (`description`, is_public, post_date, app_user_id, restaurant_id)
	values
	("I'll be back...", true, '2023-06-25 14:27:00', 2, 1),
	("It was okay.", true, '2023-05-25 14:27:00', 2, 1),
	("Getchu some.", true, '2023-02-22 14:27:00', 1, 2);

insert into rating (score, `description`, app_user_id, restaurant_id)
	values
	(5, 'Addictive', 2, 3),
	(3, 'Not for me', 2, 1),
	(5, 'My go-to', 1, 2),
    (5, 'The vibe is immaculate', 2, 4),
    (4, 'Not the same as it used to be', 1, 5);
    
insert into app_user_restaurant (app_user_id, restaurant_id, identifier)
    values
    (1, 2, '1-2'),
    (2, 1, '2-1'),
    (2, 3, '2-3'),
    (2, 4, '2-4'),
    (1, 5, '1-5');