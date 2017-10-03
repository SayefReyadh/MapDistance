SELECT * ,
ACOS( SIN( RADIANS( latitude ) ) * SIN( RADIANS( 23 ) ) + COS( RADIANS( latitude ) )
* COS( RADIANS( 23 )) * COS( RADIANS( longitude ) - RADIANS( 90 )) ) * 6380 AS distance
FROM driver_location having distance ORDER BY distance;


SELECT * ,
ACOS( SIN( RADIANS( latitude ) ) * SIN( RADIANS( user_lat ) ) + COS( RADIANS( latitude ) )
* COS( RADIANS( user_lat )) * COS( RADIANS( longitude ) - RADIANS( user_lng )) ) * 6380 AS distance
FROM driver_location having distance < radius ORDER BY distance;