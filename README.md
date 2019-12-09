### Filtration:

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
