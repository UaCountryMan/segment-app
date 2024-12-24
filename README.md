### Case 1:

Default country statistic for segment. No possibility to get segment which has no statistic by specific country. 

Request 
`http://localhost:8080/country-statistics?typeId=1&dataProviderId=1`

Will generate SQL:
```sql
    select
        cs1_0.country_code,
        cs1_0.segment_id,
        cs1_0.active_profiles_amount,
        s1_0.id,
        s1_0.is_active,
        dp1_0.id,
        dp1_0.name,
        s1_0.name,
        st1_0.id,
        st1_0.name,
        st1_0.view_name,
        cs1_0.sleeping_profiles_amount
    from
        country_stats cs1_0
    left join
        segments s1_0
            on s1_0.id=cs1_0.segment_id
    left join
        data_providers dp1_0
            on dp1_0.id=s1_0.data_provider
    left join
        segment_types st1_0
            on st1_0.id=s1_0.type_id
    where
      cs1_0.country_code=?
      and s1_0.data_provider=?
      and s1_0.type_id=?
```

And return:
```json
[
   {
      "segmentId":2,
      "countryCode":"",
      "activeProfilesAmount":null,
      "sleepingProfilesAmount":null,
      "segment":{
         "id":2,
         "name":"Segment2",
         "active":null,
         "dataProvider":{
            "id":1,
            "name":"Spring"
         },
         "segmentType":{
            "id":1,
            "name":"brand",
            "viewName":"Brand"
         }
      }
   }
]
```

### Case 2:

Default country statistic for segment with ability to search by concatenated columns.

A lot of duplicated joins. Is there any chance to remove them?

Request 
`http://localhost:8080/country-statistics?search=ment3`

Will generate SQL:
```sql
    select
        cs1_0.country_code,
        cs1_0.segment_id,
        cs1_0.active_profiles_amount,
        s2_0.id,
        s2_0.is_active,
        dp2_0.id,
        dp2_0.name,
        s2_0.name,
        st2_0.id,
        st2_0.name,
        st2_0.view_name,
        cs1_0.sleeping_profiles_amount
    from
        country_stats cs1_0
    left join
        segments s1_0
            on s1_0.id=cs1_0.segment_id
    left join
        data_providers dp1_0
            on dp1_0.id=s1_0.data_provider
    left join
        segment_types st1_0
            on st1_0.id=s1_0.type_id
    left join
        segments s2_0
            on s2_0.id=cs1_0.segment_id
    left join
        data_providers dp2_0
            on dp2_0.id=s2_0.data_provider
    left join
        segment_types st2_0
            on st2_0.id=s2_0.type_id
    where
        cs1_0.country_code=?
      and (
              (
                  (
                      dp1_0.name||?
                      )||(
                      st1_0.view_name||?
                      )
                  )||s1_0.name
              ) like ? escape ''
```

And return:
```json
[
   {
      "segmentId":3,
      "countryCode":"",
      "activeProfilesAmount":null,
      "sleepingProfilesAmount":null,
      "segment":{
         "id":3,
         "name":"Segment3",
         "active":null,
         "dataProvider":{
            "id":2,
            "name":"Looe"
         },
         "segmentType":{
            "id":2,
            "name":"test",
            "viewName":"Test"
         }
      }
   }
]
```

### Case 3:

Segment with single country statistic fetched using Criteria API. Should return statistic only by requested country.

Request 
`http://localhost:8080/segments`

Will generate SQL:
```sql
    select
        s1_0.id,
        s1_0.name,
        s1_0.is_active,
        s1_0.data_provider,
        dp1_0.name,
        s1_0.type_id,
        st1_0.name,
        st1_0.view_name,
        cs1_0.country_code,
        cs1_0.active_profiles_amount,
        cs1_0.sleeping_profiles_amount
    from
        segments s1_0
    left join
        country_stats cs1_0
            on s1_0.id=cs1_0.segment_id
            and cs1_0.country_code=?
    join
        data_providers dp1_0
            on dp1_0.id=s1_0.data_provider
    join
        segment_types st1_0
            on st1_0.id=s1_0.type_id
    where
        s1_0.id is not null
        and s1_0.type_id=?
```

And return:
```json
[
   {
      "id":2,
      "name":"Segment2",
      "active":null,
      "dataProvider":{
         "id":1,
         "name":"Spring"
      },
      "segmentType":{
         "id":1,
         "name":"brand",
         "viewName":"Brand"
      },
      "countryStats":[
         {
            "segmentId":2,
            "countryCode":"",
            "activeProfilesAmount":null,
            "sleepingProfilesAmount":null,
            "id": {
                "segmentId": 2,
                "countryCode": ""
            },
            "new": false
         }
      ]
   }
]
```

### Case 4:

Segment with single country statistic specification fetched using graph and JpaSpecificationExecutor

Unexpected behaviour. There is additional join on country_stats and return statistic for all countries by segment

Request
`http://localhost:8080/segments?jpa-repo=true`

Will generate SQL:
```sql
    select
        s1_0.id,
        s1_0.is_active,
        cs2_0.segment_id,
        cs2_0.country_code,
        cs2_0.active_profiles_amount,
        cs2_0.sleeping_profiles_amount,
        dp1_0.id,
        dp1_0.name,
        s1_0.name,
        st1_0.id,
        st1_0.name,
        st1_0.view_name
    from
        segments s1_0
    left join
        country_stats cs1_0
            on s1_0.id=cs1_0.segment_id
            and cs1_0.country_code=?
    left join
        country_stats cs2_0
            on s1_0.id=cs2_0.segment_id
    left join
        data_providers dp1_0
            on dp1_0.id=s1_0.data_provider
    left join
        segment_types st1_0
            on st1_0.id=s1_0.type_id
    where
        s1_0.id is not null
        and s1_0.type_id=?
```

And return:
```json
[
  {
    "id": 2,
    "name": "Segment2",
    "active": null,
    "dataProvider": {
      "id": 1,
      "name": "Spring"
    },
    "segmentType": {
      "id": 1,
      "name": "brand",
      "viewName": "Brand"
    },
    "countryStats": [
      {
        "segmentId": 2,
        "countryCode": "",
        "activeProfilesAmount": null,
        "sleepingProfilesAmount": null,
        "id": {
          "segmentId": 2,
          "countryCode": ""
        },
        "new": false
      },
      {
        "segmentId": 2,
        "countryCode": "US",
        "activeProfilesAmount": null,
        "sleepingProfilesAmount": null,
        "id": {
          "segmentId": 2,
          "countryCode": "US"
        },
        "new": false
      }
    ]
  }
]
```

### Ordering:

Request 
`http://localhost:8080/country-statistics`

Will generate SQL:
```sql
    select
        cs1_0.country_code,
        cs1_0.segment_id,
        cs1_0.active_profiles_amount,
        s1_0.id,
        s1_0.is_active,
        dp1_0.id,
        dp1_0.name,
        s1_0.name,
        st1_0.id,
        st1_0.name,
        st1_0.view_name,
        cs1_0.sleeping_profiles_amount
    from
        country_stats cs1_0
    left join
        segments s1_0
            on s1_0.id=cs1_0.segment_id
    left join
        data_providers dp1_0
            on dp1_0.id=s1_0.data_provider
    left join
        segment_types st1_0
            on st1_0.id=s1_0.type_id
    where
        cs1_0.country_code=?
    order by
        s1_0.id
    offset
        ? rows
    fetch
        first ? rows only;


    select
        count(*)
    from
        country_stats cs1_0
    where
        cs1_0.country_code=?;
```

And return:
```json
[
   {
      "segmentId":1,
      "countryCode":"",
      "activeProfilesAmount":null,
      "sleepingProfilesAmount":null,
      "segment":{
         "id":1,
         "name":"Segment1",
         "active":null,
         "dataProvider":{
            "id":2,
            "name":"Looe"
         },
         "segmentType":{
            "id":3,
            "name":"audience",
            "viewName":"Audience"
         }
      }
   },
   {
      "segmentId":2,
      "countryCode":"",
      "activeProfilesAmount":null,
      "sleepingProfilesAmount":null,
      "segment":{
         "id":2,
         "name":"Segment2",
         "active":null,
         "dataProvider":{
            "id":1,
            "name":"Spring"
         },
         "segmentType":{
            "id":1,
            "name":"brand",
            "viewName":"Brand"
         }
      }
   }
]
```

### Projection

Projection DTO

```java
@Data
public class SegmentProjection {
    private final Integer id;
    private final String name;
}
```

Repository method

```java
public interface SegmentRepository extends JpaRepository<Segment, Integer> {
    List<SegmentProjection> findByNameLike(String name);
}
```

Query
```sql
    select
        s1_0.id,
        s1_0.name
    from
        segments s1_0
    where
        s1_0.name like ? escape '\'
```