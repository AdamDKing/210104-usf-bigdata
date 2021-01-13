--explore the reports_to column in employee a bit
select first_name, last_name, title, employee_id , reports_to from employee;

-- start with select, gets us all the columns from artist
select * from artist;

-- can specify columns
select name from artist;

-- || is string concatenation, and in general we can modify the columns we select on the fly
-- can alias with AS
select first_name || ' ' || last_name as "Name" from customer;

-- String literals in sql are denoted with '', "" are for identifiers for columns, tables, schema, ...

-- We almost always use both select and from in our querying
-- then there are optional clauses that follow from for advanced querying

-- 6+ clauses:
-- SELECT specifies columns
-- FROM specifies table (and more)
-- WHERE filters based on records
select *
from customer
where city = 'Prague';

select *
from genre
where length(name) >= 6;

-- length above is an example of a built in scalar function
-- a function that takes a single value and returns a value
-- we specify "scalar" function to disambiguate from "aggregate"
-- functions that take a group of values

-- The next clause is GROUP BY.  This lets us combine records into groups
-- and retrieve results based on those groups, rather than individual records.
-- we use GROUP BY with "aggregate" functions, that operate on the groups produced
-- basic aggregate functions: COUNT(), MIN(), MAX(), SUM(), ...
-- When we use group by we specify a column or set of columns (or an expression on columns)
-- all records with the same value are grouped together

-- treats the entire result set as a single group
select count(*) from customer;


select country, count(*)
from customer
group by country;

select country, count(*)
from customer
where city = 'Prague'
group by country;

-- quick note: use like 'string' for basic string pattern matching.  _ is a single character wildcard,
-- % is one or more character wildcard.  In postgres, SIMILAR TO will let you use (slightly modified) regex

-- count of employees by city
select city, count(*)
from employee
group by city;

-- earliest hire among employees in each city
select city, min(hire_date)
from employee e 
group by city;

-- count by state among customers with a first name starting with A
select state, count(*)
from customer
where first_name like 'A%'
group by state;

--check the prior result
select *
from customer
where first_name like 'A%';

-- the next clause is HAVING
-- HAVING filters your groups, similar to the way WHERE filters your records
-- we can filter out groups of countries that only have a single customer:
select country, count(*)
from customer
group by country
having count(*) > 1;

-- we can use both HAVING and WHERE -- where will filter records and having will filter groups

-- aside: pointless subquery, but basic subquery syntax:
select *
from ( select * from customer ) as demo
where city != 'Prague';

--retrieve all records except the ones with city Pargue or Paris, grouping those by country, then filtering out groups
-- containing a single record.
select country, count(*)
from customer c --this customer c autocomplete will let us use c to refer to the customer table later in the query
where city not in ('Prague', 'Paris')
group by country
having count(*) > 1;

-- after HAVING, we have ORDER BY, LIMIT, and OFFSET
-- ORDER BY will sort the results based on a column or expression
-- LIMIT will limit the number of results in the resultset
-- OFFSET will start providing results in the resultset after skipping a number of them
select country, count(*)
from customer c --this customer c autocomplete will let us use c to refer to the customer table later in the query
where city not in ('Prague', 'Paris')
group by country
having count(*) > 1
order by count(*)
offset 3;

-- select employees ordered by hire date
select *
from employee e 
order by hire_date;

-- select the employee that was hired earliest
select *
from employee e 
order by hire_date 
limit 1;

-- select the employees hired 2nd, 3rd, and 4th
select *
from employee e
order by hire_date
limit 3
offset 1;

-- aside: quick exercises!
-- SQL SELECT exercises, some require looking things up!
-- 1. list all customers (full names, customer ID, and country)
--    who are not in the US.
select first_name || ' ' || last_name as "full name", customer_id, country
from customer
where country != 'USA';
-- 2. list all customers from brazil.
select first_name || ' ' || last_name as "full name", customer_id, country
from customer
where country = 'Brazil';
-- 3. list all sales agents.
select * from employee e2
where title = 'Sales Support Agent';
-- 4. show a list of all countries in billing addresses on invoices.
--    (there should not be duplicates in the list, figure out how to do that)
select distinct billing_country from invoice; 
-- 5. how many invoices were there in 2009, and what was the
--    sales total for that year? what about 2011 (can use a separate query)?
-- The EXTRACT() function is useful for 5.
select extract(year from invoice_date) as invoice_year, count(*), sum(total) as "sales total" 
from invoice
group by invoice_year;
-- 6. how many line items were there for invoice #37?
select count(*) as "Invoice Lines For Invoice 37"
from invoice_line il 
where invoice_id = 37;
-- 7. how many invoices per country?
select billing_country, count(*)
from invoice i2
group by billing_country;
-- 8. show total sales per country,
--    ordered by highest sales first (figure out how to do that)
select billing_country, sum(total)
from invoice i 
group by billing_country
order by sum(total) desc;

-- Lets talk a bit about joins since they keep coming up
-- When we store normalized data, we tend to split our data up into many different tables

-- If we wanted to list a playlist with track, album, and artist, we would need information
-- from 5 different tables in chinook.
-- Now, there's are multiple good reasons to normalize your data, so this isn't a problem
-- we just need a way to easily put together the data from across different tables
-- when we want to use it.

-- Joins are the most important tool for this.  Here we're going to demo/discuss joins as they
-- are used in 95-99% of use cases.  That means we're discussing "inner joins on foreign key relationships"

-- Joins let us combine records from multiple tables
-- when you join, you produce a resultset with more columns in the output.

-- we want to produce an "itunes view" with playlist, track, album, artist

-- this just gets up playlist
select * from playlist;

-- playlists are joined to tracks through the playlist_track junction table
-- this gets us playlists and associated tracks
select *
from playlist p 
  inner join playlist_track pt on p.playlist_id = pt.playlist_id
  inner join track t on pt.track_id = t.track_id 
where p."name" != 'Music';

-- this adds albums to our playlists + tracks
select *
from playlist p 
  inner join playlist_track pt on p.playlist_id = pt.playlist_id
  inner join track t on pt.track_id = t.track_id 
  inner join album a on t.album_id = a.album_id 
where p."name" != 'Music';

-- this adds artists to our playlists + tracks + albums
-- it also cleans up the columns a bit with aliases

-- we may want to save this query and use it elsewhere.  The tool we should use is
-- a VIEW.  A VIEW is essentially a saved query with a name.  We can also
-- have MATERIALIZED VIEWS which are like saved resultsets from a query with a name.
create view itunes as
select p.name as "playlist", t.name as "track", a.title as "album", ar.name as "artist"
from playlist p 
  inner join playlist_track pt on p.playlist_id = pt.playlist_id
  inner join track t on pt.track_id = t.track_id 
  inner join album a on t.album_id = a.album_id
  inner join artist ar on a.artist_id = ar.artist_id 
where p."name" != 'Music';

select * from itunes;

-- a bit of DML to update Aquaman
update artist set name = 'Aquaman Two' where name = 'Aquaman';
-- that edit shows up in the view, since the view queries the underlying tables

-- since itunes is a view, like a saved query, the query is re-run every time we use itunes.
-- we can also create materialized views, which are like saved resultsets.  These will be saved and
-- they won't hit the underlying tables when we use them.  Materialized views can provide huge efficiency
-- gains, but they won't automatically update.
create materialized view itunes_mat as
select p.name as "playlist", t.name as "track", a.title as "album", ar.name as "artist"
from playlist p 
  inner join playlist_track pt on p.playlist_id = pt.playlist_id
  inner join track t on pt.track_id = t.track_id 
  inner join album a on t.album_id = a.album_id
  inner join artist ar on a.artist_id = ar.artist_id 
where p."name" != 'Music';

select * from itunes_mat;

-- if we do a little DML and update:
update artist set name = 'Aquaman Three' where name = 'Aquaman Two';
-- those results will *not* show up in the materialized view, since it saves the resultset
-- and doesn't repeatedly hit the underlying tables.

-- we can force an update with:
refresh materialized view itunes_mat;








