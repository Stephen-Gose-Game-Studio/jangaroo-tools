package net.jangaroo.jooc.mvnplugin.proxy;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Add support for matrix parameters and SSL.
 */
public class JangarooProxyServlet extends ProxyServlet {

  private static final X509TrustManager TRUST_MANAGER = new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  };

  @Override
  protected String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
    StringBuilder uri = new StringBuilder(500);
    uri.append(getTargetUri(servletRequest));

    // use getRequestURI instead of getPathInfo to preserve matrix parameters
    String servletPath = servletRequest.getServletPath();
    String relativeRequestUri = servletRequest.getRequestURI().replaceFirst(servletPath, "");
    uri.append(encodeUriQuery(relativeRequestUri));

    // Handle the query string & fragment
    String queryString = servletRequest.getQueryString();//ex:(following '?'): name=value&foo=bar#fragment
    String fragment = null;
    //split off fragment from queryString, updating queryString if found
    if (queryString != null) {
      int fragIdx = queryString.indexOf('#');
      if (fragIdx >= 0) {
        fragment = queryString.substring(fragIdx + 1);
        queryString = queryString.substring(0, fragIdx);
      }
    }

    queryString = rewriteQueryStringFromRequest(servletRequest, queryString);
    if (queryString != null && queryString.length() > 0) {
      uri.append('?');
      uri.append(encodeUriQuery(queryString));
    }

    if (doSendUrlFragment && fragment != null) {
      uri.append('#');
      uri.append(encodeUriQuery(fragment));
    }

    return uri.toString();
  }

  /**
   * {@inheritDoc}
   * <p>
   * Add support for proxying to https. All certificates are accepted.
   * </p>
   */
  @Override
  protected HttpClient createHttpClient(final RequestConfig requestConfig) {
    SSLContext ctx;
    try {
      ctx = SSLContext.getInstance("TLS");
      ctx.init(null, new TrustManager[]{TRUST_MANAGER}, null);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }

    return HttpClients.custom()
            .useSystemProperties()
            .setSSLSocketFactory(new SSLConnectionSocketFactory(ctx))
            .setDefaultRequestConfig(requestConfig)
            .build();
  }

  /**
   * {@inheritDoc}
   * <p>
   * Only set the secure flag when the incoming request is already https to allow proxying from http to https.
   * </p>
   */
  @Override
  protected void copyProxyCookie(HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse, String headerValue) {
    List<HttpCookie> cookies = HttpCookie.parse(headerValue);
    String path = servletRequest.getContextPath(); // path starts with / or is empty string
    path += servletRequest.getServletPath(); // servlet path starts with / or is empty string
    if (path.isEmpty()) {
      path = "/";
    }

    for (HttpCookie cookie : cookies) {
      //set cookie name prefixed w/ a proxy value so it won't collide w/ other cookies
      String proxyCookieName = doPreserveCookies ? cookie.getName() : getCookieNamePrefix(cookie.getName()) + cookie.getName();
      Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
      servletCookie.setComment(cookie.getComment());
      servletCookie.setMaxAge((int) cookie.getMaxAge());
      servletCookie.setPath(path); //set to the path of the proxy servlet
      servletCookie.setHttpOnly(cookie.isHttpOnly());
      servletCookie.setSecure("https".equals(servletRequest.getScheme()) && cookie.getSecure());
      servletCookie.setVersion(cookie.getVersion());
      servletResponse.addCookie(servletCookie);
    }
  }
}