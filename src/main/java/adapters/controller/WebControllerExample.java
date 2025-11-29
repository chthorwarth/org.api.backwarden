//package adapters.api;
//
//import io.netty.handler.codec.http.HttpResponseStatus;
//import jakarta.inject.Inject;
//import jakarta.validation.Valid;
//import jakarta.validation.Validator;
//import jakarta.validation.constraints.Positive;
//import jakarta.validation.constraints.PositiveOrZero;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.*;
//
//import java.util.List;
//
//@Path( "universities" )
//public class WebControllerExample
//{
//    @Inject
//    private UniversityServiceAdapter universityServiceAdapter;
//
//    @Inject
//    private Validator validator;
//
//    @Context
//    private UriInfo uriInfo;
//
//    @Context
//    private HttpHeaders httpHeaders;
//
//    @GET
//    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//    public Response getAllUniversities(
//            @DefaultValue( "" ) @QueryParam( "q" ) String query,
//            @PositiveOrZero @DefaultValue( "0" ) @QueryParam( "offset" ) long offset,
//            @Positive @DefaultValue( "20" ) @QueryParam( "size" ) long size
//    )
//    {
//        final var requestedUniversities = this.universityServiceAdapter.getAllUniversities( ).getUniversityDTOs( );
//        addSelfLinksToUniversities( requestedUniversities );
//        return Response.status( HttpResponseStatus.OK.code( ) )
//                .header( "X-Total-Count", requestedUniversities.size( ) )
//                .entity( requestedUniversities ).build( );
//    }
//
//    @Path( "{id}" )
//    @GET
//    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//    public Response getById( @Positive @PathParam( "id" ) long id )
//    {
//        final var requestedUniversity = this.universityServiceAdapter.getUniversityById( id ).getUniversityDTO( );
//        addSelfLinkToUniversity( requestedUniversity );
//        return Response.ok( requestedUniversity ).build( );
//    }
//
//    @POST
//    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//    public Response createUniversity( @Valid UniversityDTO model )
//    {
//        final var result = this.universityServiceAdapter.createNewUniversity( model );
//        return Response.status( HttpResponseStatus.CREATED.code( ) )
//                .header( "Location", createLocationHeader( result.getUniversityDTO( ) ) )
//                .build( );
//    }
//
//    @PUT
//    @Path( "{id}" )
//    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//    public Response updateUniversity( @Positive @PathParam( "id" ) long id, @Valid UniversityDTO model )
//    {
//        this.universityServiceAdapter.updateUniversity( id, model );
//        return Response.status( HttpResponseStatus.NO_CONTENT.code( ) ).build( );
//    }
//
//    @DELETE
//    @Path( "{id}" )
//    public Response deleteUniversity( @Positive @PathParam( "id" ) long id )
//    {
//        this.universityServiceAdapter.deleteUniversity( id );
//        return Response.status( HttpResponseStatus.NO_CONTENT.code( ) ).build( );
//    }
//
//    private String createLocationHeader( UniversityDTO model )
//    {
//        return uriInfo.getRequestUriBuilder( ).path( Long.toString( model.getId( ) ) ).build( ).toString( );
//    }
//
//    private void addSelfLinksToUniversities( List<UniversityDTO> models )
//    {
//        models.forEach( this::addSelfLinkToUniversity );
//    }
//
//    private void addSelfLinkToUniversity( UniversityDTO university )
//    {
//        final var currentUri = uriInfo.getAbsolutePath( );
//        final var path = currentUri.getPath( );
//        final var newPath = path.replaceFirst( "/\\d*$", "" );
//        final var newUri = UriBuilder.fromUri( currentUri )
//                .replacePath( newPath + "/" + university.getId( ) )
//                .build( );
//
//        university.getSelfLink( ).setHref( newUri.toASCIIString( ) );
//        university.getSelfLink( ).setRel( "self" );
//        university.getSelfLink( ).setType( httpHeaders.getHeaderString( "Accept" ) );
//    }
//}
