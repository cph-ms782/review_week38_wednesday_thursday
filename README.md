# Review week38 wednesday thursday  

## [REST with JAX RS](https://docs.google.com/document/d/19km0ZoaAX0k_stnYOWfAZPd4wXbTGMWhme1xZopj-PA/edit#)  
### The facade and the matching endpoints
Læringsmål:

  * Kunne demonstrere kode med grundlægende JPQL Queries  
  * Kunne redegøre for konventioner ved opbygning af REST api'er  
  * Kunne håndtere parametre i REST endpoints i egne kodeeksempler  
  * Kunne implementere GET, POST, PUT og DELETE requests  
  * Kunne redegøre for, og implementere DTO's og konvertere frem og tilbage mellem Java og JSON  
  * Kunne vise med egne kodeeksempler, hvordan man håndterer POST med tilhørende JSON og får det deserialiseret til Java entiteter
 

 1) Use our start code as the starting point for this exercise. Initially, just skip the steps related to Travis and Deployment. It will provide you with a starting point for the Rest Assured tests you have to write.  

 2) Create a person database and, either use your existing, or create a new test database for this exercise.  
_***Person DB created on localhost**_

 3) Implement a facade class that implements the IPersonFacade given above. Initially just add dummy methods, since we like you to implement the methods, step-by-step, including their matching REST-endpoints. When you implement the methods, make sure to create corresponding JUnit tests.  
[Facade](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/master/The-facade-and-the-matching-endpoints/src/main/java/facades/PersonFacade.java)  

 4) Complete the GET methods in the facade, and implementing matching REST endpoints. Test via a browser or Postman (Rest Assured will be requested later).  
 [Facade](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/facades/PersonFacade.java#L69)  
 [REST](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/rest/PersonResource.java#L28)  

 5) Complete the addPerson(..) method in the facade, and implement a matching REST endpoint. Test using Postman  
 [Facade](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/facades/PersonFacade.java#L41)  
 [REST](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/rest/PersonResource.java#L50)  

 6) Complete the editPerson(..) method in the facade, and implement a matching REST endpoint. Test using Postman  
 [Facade](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/facades/PersonFacade.java#L91)  
 [REST](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/rest/PersonResource.java#L59)  
  
 7)  Complete the deletePerson(..) method in the facade, and implement a matching REST endpoint. Test using Postman  
 [Facade](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/facades/PersonFacade.java#L55)  
 [REST](https://github.com/cph-ms782/review_week38_wednesday_thursday/blob/36f8cb6a6383d0e33e637f8983d4668a8534e855/The-facade-and-the-matching-endpoints/src/main/java/rest/PersonResource.java#L69)  

### Error Handling with JAX RS and ExceptionMappers  
1) Add the following Exceptions to the system  


2) Change the interface (and implementation) as sketched  



3) Test your error responses, first using Postman, and then with JUnit and Rest Assured.  



