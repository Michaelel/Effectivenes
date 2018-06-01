package network;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Message {
	public final static int MAX_BODY_SIZE = 4096;

	public class Header {
		public final static String CONTENT_LENGTH_KEY = "Content-Length";
		public final static String REQUEST_ACTION_KEY = "Request-Action";
		public final static String RESPONSE_CODE_KEY = "Response-Code";

		public final static int OK_RESPONSE_CODE = 200;
		public final static int ERROR_RESPONSE_CODE = 400;
		
		public final static int HEADER_SIZE = 128;
		public final static String HEADER_STR_VALUE_NONE = "";
		public final static int HEADER_INT_VALUE_NONE = -1;

		private final static String HEADER_DELIMETER = "\n";
		private final static String HEADER_ENTRY_DELIMETER = "::";

		private String requestAction = HEADER_STR_VALUE_NONE;
		private int contentLength = 0;
		private int responseCode = HEADER_INT_VALUE_NONE;

		private void parse(String headerStr) {
			String[] headers = headerStr.split(HEADER_DELIMETER);
			for (String header : headers) {
				String[] keyValue = header.split(HEADER_ENTRY_DELIMETER);
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				switch (key) {
				case CONTENT_LENGTH_KEY: {
					contentLength = Integer.parseInt(value);
					break;
				}
				case REQUEST_ACTION_KEY: {
					requestAction = value;
					break;
				}
				case RESPONSE_CODE_KEY: {
					responseCode = Integer.parseInt(value);
					break;
				}
				default:
					System.out.println("Unrecognized header entry: " + key);
				}
			}
		}

		public String getRequestAction() {
			return requestAction;
		}

		public int getContentLength() {
			return contentLength;
		}

		public int getResponseCode() {
			return responseCode;
		}

		@Override
		public String toString() {
			StringBuilder headerStrBuilder = new StringBuilder();
			if (header.requestAction != Header.HEADER_STR_VALUE_NONE) {
				headerStrBuilder
						.append(Header.REQUEST_ACTION_KEY + Header.HEADER_ENTRY_DELIMETER + header.requestAction);
				headerStrBuilder.append(Header.HEADER_DELIMETER);
			}
			if (header.responseCode != Header.HEADER_INT_VALUE_NONE) {
				headerStrBuilder.append(Header.RESPONSE_CODE_KEY + Header.HEADER_ENTRY_DELIMETER + header.responseCode);
				headerStrBuilder.append(Header.HEADER_DELIMETER);
			}
			headerStrBuilder.append(Header.CONTENT_LENGTH_KEY + Header.HEADER_ENTRY_DELIMETER + header.contentLength);
			return headerStrBuilder.toString();
		}
	}

	private Header header;
	private String body = "";
	private int clientSocketId;

	private MessageBuilder builder;

	public Message() {
		builder = new MessageBuilder(this);
		header = new Header();
	}

	public class MessageBuilder {
		private Message message;

		private int nbytes;
		private List<byte[]> dataParts = new ArrayList<byte[]>();

		private List<byte[]> restBytes = new ArrayList<byte[]>();
		
		private MessageBuilder(Message message) {
			this.message = message;
		}

		public void attach(byte[] data) {
			nbytes += data.length;
			if (headerIsReady()) {
				if (remainingBytes() == 0) {
					restBytes.add(data);
					return;
				}
				String dataStr = new String(data, StandardCharsets.UTF_8);
				int dataLength = dataStr.length() < remainingBytes() ? dataStr.length() : remainingBytes();
				body += dataStr.substring(0, dataLength);
				restBytes.add(dataStr.substring(dataLength).getBytes());
				return;
			}
			dataParts.add(data);
			if (nbytes >= Header.HEADER_SIZE) {
				buildHeader();
			}
		}

		private int remainingBytes() {
			return message.header.contentLength - body.length();
		}
		
		private void buildHeader() {
			StringBuilder strBuilder = new StringBuilder();
			for (Iterator<byte[]> dataPartIter = dataParts.iterator(); dataPartIter.hasNext();) {
				strBuilder.append(new String(dataPartIter.next(), StandardCharsets.UTF_8));
				dataPartIter.remove();
			}
			String bytes = strBuilder.toString();
			String headerStr = bytes.substring(0, Header.HEADER_SIZE);
			header.parse(headerStr);
			attach(bytes.substring(Header.HEADER_SIZE).getBytes());
		}

		private boolean isReady() {
			return headerIsReady() && message.header.contentLength == body.length();
		}

		private boolean headerIsReady() {
			return (message.header.requestAction != Header.HEADER_STR_VALUE_NONE
					|| message.header.responseCode != Header.HEADER_INT_VALUE_NONE);
		}

		private MessageBuilder setContentLength(int contentLength) {
			message.header.contentLength = contentLength;
			return this;
		}

		public MessageBuilder setResponseCode(int responseCode) {
			message.header.responseCode = responseCode;
			return this;
		}

		public MessageBuilder setRequestAction(String requestAction) {
			message.header.requestAction = requestAction;
			return this;
		}

		public MessageBuilder setBody(String body) {
			setContentLength(body.length());
			message.body = body;
			return this;
		}
		
		public List<byte[]> getRestBytes() {
			return restBytes;
		}
	}

	public void read(byte[] data) {
		if (data == null)
			return;
		builder.attach(data);
	}

	public void write(ByteBuffer destBuffer) {
		if (!isReady())
			throw new RuntimeException("Message is not ready to be written");

		String headerStr = header.toString();
		destBuffer.put(headerStr.getBytes(StandardCharsets.UTF_8));
		for (int i = 0; i < Header.HEADER_SIZE - headerStr.length(); ++i)
			destBuffer.put((byte) ' ');
		destBuffer.put(body.getBytes(StandardCharsets.UTF_8));
	}

	public boolean isReady() {
		return builder.isReady();
	}

	public Header getHeader() {
		return header;
	}

	public MessageBuilder getBuilder() {
		return builder;
	}

	public String getBody() {
		return body;
	}

	@Override
	public String toString() {
		StringBuilder headerStrBuilder = new StringBuilder();
		if (header.requestAction != Header.HEADER_STR_VALUE_NONE) {
			headerStrBuilder.append(Header.REQUEST_ACTION_KEY + Header.HEADER_ENTRY_DELIMETER + header.requestAction);
			headerStrBuilder.append(Header.HEADER_DELIMETER);
		}
		if (header.responseCode != Header.HEADER_INT_VALUE_NONE) {
			headerStrBuilder.append(Header.RESPONSE_CODE_KEY + Header.HEADER_ENTRY_DELIMETER + header.responseCode);
			headerStrBuilder.append(Header.HEADER_DELIMETER);
		}
		headerStrBuilder.append(Header.CONTENT_LENGTH_KEY + Header.HEADER_ENTRY_DELIMETER + header.contentLength);
		return headerStrBuilder.toString();
	}

	public int length() {
		if (!isReady())
			return -1;
		return Header.HEADER_SIZE + header.contentLength;
	}

	public int getClientSocketId() {
		return clientSocketId;
	}

	public void setClientSocketId(int clientSocketId) {
		this.clientSocketId = clientSocketId;
	}
}
