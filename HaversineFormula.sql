SELECT * ,
ACOS( SIN( RADIANS( latitude ) ) * SIN( RADIANS( 23 ) ) + COS( RADIANS( latitude ) )
* COS( RADIANS( 23 )) * COS( RADIANS( longitude ) - RADIANS( 90 )) ) * 6371 AS distance
FROM driver_location having distance < 100 ORDER BY distance;


SELECT * ,
ACOS( SIN( RADIANS( latitude ) ) * SIN( RADIANS( user_lat ) ) + COS( RADIANS( latitude ) )
* COS( RADIANS( user_lat )) * COS( RADIANS( longitude ) - RADIANS( user_lng )) ) * 6371 AS distance
FROM driver_location having distance < radius ORDER BY distance;
