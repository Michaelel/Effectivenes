package repository;

public class RepositoryManager {
	static {
		SqliteDB.connect();
	}
	
	private static MemberRepository memberRepository = new MemberRepository();
	private static ChannelRepository channelRepository = new ChannelRepository();
	private static MessageRepository messageRepository = new MessageRepository();
	
	public static MemberRepository getMemberRepository() {
		return memberRepository;
	}

	public static ChannelRepository getChannelRepository() {
		return channelRepository;
	}

	public static MessageRepository getMessageRepository() {
		return messageRepository;
	}
}
