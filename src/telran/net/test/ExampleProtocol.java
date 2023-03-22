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
			response = new Response(ResponseCode.WRONG_DATA, null);
		} else {
			response = switch (type) {
				case("reverse") -> new Response(ResponseCode.OK, new StringBuilder(data).reverse().toString());
				case("length") -> new Response(ResponseCode.OK, data.length() + "");
				default -> new Response(ResponseCode.WRONG_REQUEST, null);
			};
		}

		return response;
	}

}
