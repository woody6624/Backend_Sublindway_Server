package SublindWay_server.Dto;

public interface OAuth2MemberInfo {
    String getProviderId();

    String getProvider();

    String getName();

    String getEmail();

    String getRole();
}
