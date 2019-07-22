<%
    String redirectURL = request.getContextPath()+"/History?page=dashboard";
    response.sendRedirect(redirectURL);
%>