package rebelsrescue;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

public class SwapiUrlTransformer extends ResponseTransformer {
    @Override
    public String getName() {
        return "swapiUrlTransformer";
    }

    @Override
    public Response transform(Request request, Response response, FileSource fileSource, Parameters parameters) {
        var transformedResponse =
                response.getBodyAsString().replaceAll("http://swapi", "http://localhost:%d".formatted(request.getPort()));

        return Response.Builder.like(response).but().body(transformedResponse).build();
    }
}
