### Case 1:

Request 
`http://localhost:8080/segments/stats?typeId=1&dataProviderId=1`

Will generate SQL:
```sql
    select
        countrysta0_.country_code as country_1_0_0_,
        countrysta0_.segment_id as segment_2_0_0_,
        segment3_.id as id1_3_1_,
        dataprovid4_.id as id1_1_2_,
        segmenttyp5_.id as id1_2_3_,
        countrysta0_.active_profiles_amount as active_p3_0_0_,
        countrysta0_.sleeping_profiles_amount as sleeping4_0_0_,
        segment3_.is_active as is_activ2_3_1_,
        segment3_.data_provider as data_pro4_3_1_,
        segment3_.name as name3_3_1_,
        segment3_.type_id as type_id5_3_1_,
        dataprovid4_.name as name2_1_2_,
        segmenttyp5_.name as name2_2_3_,
        segmenttyp5_.view_name as view_nam3_2_3_ 
    from country_stats countrysta0_ 
    left outer join segments segment3_ on countrysta0_.segment_id=segment3_.id 
    left outer join data_providers dataprovid4_ on segment3_.data_provider=dataprovid4_.id 
    left outer join segment_types segmenttyp5_ on segment3_.type_id=segmenttyp5_.id 
    cross join segments segment1_ 
    where
        countrysta0_.segment_id=segment1_.id 
        and segment1_.type_id=1 
        and segment1_.data_provider=1 
        and countrysta0_.country_code=?
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

Request 
`http://localhost:8080/segments/stats?search=ment3`

Will generate SQL:
```sql
    select
            countrysta0_.country_code as country_1_0_0_,
            countrysta0_.segment_id as segment_2_0_0_,
            segment1_.id as id1_3_1_,
            dataprovid2_.id as id1_1_2_,
            segmenttyp3_.id as id1_2_3_,
            countrysta0_.active_profiles_amount as active_p3_0_0_,
            countrysta0_.sleeping_profiles_amount as sleeping4_0_0_,
            segment1_.is_active as is_activ2_3_1_,
            segment1_.data_provider as data_pro4_3_1_,
            segment1_.name as name3_3_1_,
            segment1_.type_id as type_id5_3_1_,
            dataprovid2_.name as name2_1_2_,
            segmenttyp3_.name as name2_2_3_,
            segmenttyp3_.view_name as view_nam3_2_3_ 
        from
            country_stats countrysta0_ 
        left outer join
            segments segment1_ 
                on countrysta0_.segment_id=segment1_.id 
        left outer join
            data_providers dataprovid2_ 
                on segment1_.data_provider=dataprovid2_.id 
        left outer join
            segment_types segmenttyp3_ 
                on segment1_.type_id=segmenttyp3_.id 
        where
            (
                (
                    (
                        (
                            dataprovid2_.name||?
                        )||(
                            segmenttyp3_.view_name||?
                        )
                    )||segment1_.name
                ) like ?
            ) 
            and countrysta0_.country_code=?
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

Request 
`http://localhost:8080/segments`

Will generate SQL:
```sql
    select
        segment0_.id as id1_3_0_,
        countrysta1_.country_code as country_1_0_1_,
        countrysta1_.segment_id as segment_2_0_1_,
        segmenttyp2_.id as id1_2_2_,
        dataprovid3_.id as id1_1_3_,
        segment0_.is_active as is_activ2_3_0_,
        segment0_.data_provider as data_pro4_3_0_,
        segment0_.name as name3_3_0_,
        segment0_.type_id as type_id5_3_0_,
        countrysta1_.active_profiles_amount as active_p3_0_1_,
        countrysta1_.sleeping_profiles_amount as sleeping4_0_1_,
        countrysta1_.segment_id as segment_2_0_0__,
        countrysta1_.country_code as country_1_0_0__,
        segmenttyp2_.name as name2_2_2_,
        segmenttyp2_.view_name as view_nam3_2_2_,
        dataprovid3_.name as name2_1_3_ 
    from
        segments segment0_ 
    left outer join
        country_stats countrysta1_ 
            on segment0_.id=countrysta1_.segment_id 
            and (
                countrysta1_.country_code=?
            ) 
    left outer join
        segment_types segmenttyp2_ 
            on segment0_.type_id=segmenttyp2_.id 
    left outer join
        data_providers dataprovid3_ 
            on segment0_.data_provider=dataprovid3_.id 
    where
        segment0_.type_id=1 
        and (
            segment0_.id is not null
        )
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
            "sleepingProfilesAmount":null
         }
      ]
   }
]
```