CREATE TABLE temp.mentorsT(
first_name text,
last_name text,
nick_name text,
phone_num text,
email text,
city text,
fav_num int);
CREATE TABLE mentors(
ID INTEGER Primary Key,
first_name text,
last_name text,
nick_name text,
phone_num text,
email text,
city text,
fav_num int);
CREATE TABLE temp.applicantsT(
first_name text,
last_name text,
phone_num text,
email text,
application_code int);
CREATE TABLE applicants(
ID INTEGER Primary Key,
first_name text,
last_name text,
phone_num text,
email txt,
application_code int);
