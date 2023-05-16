package com.redhat.demo.configuration.monolith.resources

import com.redhat.demo.core.usecases.v1.person.*
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/api/people")
class PersonResource(
    private val createPersonUseCase: CreatePersonUseCase,
    private val updatePersonUseCase: UpdatePersonUseCase,
    private val deletePersonUseCase: DeletePersonUseCase,
    private val getPersonUseCase: GetPersonUseCase,
    private val searchPeopleUseCase: SearchPeopleUseCase
) {
    @POST
    @Consumes(value = [MediaType.APPLICATION_JSON])
    fun createPerson(data: RequestData): Response {
        try {
            return Response.ok(
                createPersonUseCase.execute(
                    CreatePersonUseCase.Request(
                        firstName = data.firstName,
                        lastName = data.lastName,
                        birthDate = data.birthDate
                    )
                ).ref
            ).build()
        } catch (e: CreatePersonUseCase.ValidationException) {
            return Response.status(422, e.localizedMessage).build()
        }
    }

    @PUT
    @Path("/{ref}")
    @Consumes(value = [MediaType.APPLICATION_JSON])
    fun updatePerson(@PathParam("ref") ref: String, data: RequestData): Response {
        try {
            return Response.ok(
                updatePersonUseCase.execute(
                    UpdatePersonUseCase.Request(
                        ref = ref,
                        firstName = data.firstName,
                        lastName = data.lastName,
                        birthDate = data.birthDate
                    )
                ).ref
            ).build()
        } catch (e: CreatePersonUseCase.ValidationException) {
            return Response.status(422, e.localizedMessage).build()
        }
    }

    @DELETE
    @Path("/{ref}")
    @Produces(value = [MediaType.APPLICATION_JSON])
    fun deletePerson(@PathParam("ref") ref: String): Response {
        try {
            deletePersonUseCase.execute(DeletePersonUseCase.Request(ref = ref))
            return Response.ok().build()
        } catch (e: CreatePersonUseCase.ValidationException) {
            return Response.status(422, e.localizedMessage).build()
        }
    }

    @GET
    @Path("/{ref}")
    @Produces(value = [MediaType.APPLICATION_JSON])
    fun getPerson(@PathParam("ref") ref: String): Response {
        try {
            return Response.ok(getPersonUseCase.execute(GetPersonUseCase.Request(ref = ref)).person).build()
        } catch (e: CreatePersonUseCase.ValidationException) {
            return Response.status(422, e.localizedMessage).build()
        }
    }

    @GET
    fun searchPeople(): Response {
        try {
            return Response.ok(searchPeopleUseCase.execute(SearchPeopleUseCase.Request()).people).build()
        } catch (e: SearchPeopleUseCase.ValidationException) {
            return Response.status(422, e.localizedMessage).build()
        }
    }

    open class RequestData(
        var firstName: String?,
        var lastName: String?,
        var birthDate: String?
    )
}