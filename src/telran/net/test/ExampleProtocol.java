package telran.net.test;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class ExampleProtocol implements Protocol {

	@Override
	public Response getResponse(Request request) {
		String type = request.type;
		String data = (String) request.data;
		Response response;
		
		if (data == null) {
			response = new Response(ResponseCode.WRONG_DATA, "Instead string got null in request");
		} else {
			response = switch (type) {
				case("reverse") -> new Response(ResponseCode.OK, new StringBuilder(data).reverse().toString());
				case("length") -> new Response(ResponseCode.OK, data.toString().length());
				default -> new Response(ResponseCode.WRONG_REQUEST, "Unrecognisable type of request: " + request.type);
			};
		}

		return response;
	}

}
