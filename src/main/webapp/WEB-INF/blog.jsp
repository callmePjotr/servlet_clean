

<%--
  Created by IntelliJ IDEA.
  User: llk02
  Date: 05.10.2022
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="org.owasp.esapi.ESAPI" %>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>


<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        body{
            margin-top:20px;
            background-color:#e9ebee;
        }

        .be-comment-block {
            margin-bottom: 50px !important;
            border: 1px solid #edeff2;
            border-radius: 2px;
            padding: 50px 70px;
            border:1px solid #ffffff;
        }

        .comments-title {
            font-size: 16px;
            color: #262626;
            margin-bottom: 15px;
            font-family: 'Conv_helveticaneuecyr-bold';
        }

        .be-img-comment {
            width: 60px;
            height: 60px;
            float: left;
            margin-bottom: 15px;
        }

        .be-ava-comment {
            width: 60px;
            height: 60px;
            border-radius: 50%;
        }

        .be-comment-content {
            margin-left: 80px;
        }

        .be-comment-content span {
            display: inline-block;
            width: 49%;
            margin-bottom: 15px;
        }

        .be-comment-name {
            font-size: 13px;
            font-family: 'Conv_helveticaneuecyr-bold';
        }

        .be-comment-content a {
            color: #383b43;
        }

        .be-comment-content span {
            display: inline-block;
            width: 49%;
            margin-bottom: 15px;
        }

        .be-comment-time {
            text-align: right;
        }

        .be-comment-time {
            font-size: 11px;
            color: #b4b7c1;
        }

        .be-comment-text {
            font-size: 13px;
            line-height: 18px;
            color: #7a8192;
            display: block;
            background: #f6f6f7;
            border: 1px solid #edeff2;
            padding: 15px 20px 20px 20px;
        }

        .form-group.fl_icon .icon {
            position: absolute;
            top: 1px;
            left: 16px;
            width: 48px;
            height: 48px;
            background: #f6f6f7;
            color: #b5b8c2;
            text-align: center;
            line-height: 50px;
            -webkit-border-top-left-radius: 2px;
            -webkit-border-bottom-left-radius: 2px;
            -moz-border-radius-topleft: 2px;
            -moz-border-radius-bottomleft: 2px;
            border-top-left-radius: 2px;
            border-bottom-left-radius: 2px;
        }

        .form-group .form-input {
            font-size: 13px;
            line-height: 50px;
            font-weight: 400;
            color: #b4b7c1;
            width: 100%;
            height: 50px;
            padding-left: 20px;
            padding-right: 20px;
            border: 1px solid #edeff2;
            border-radius: 3px;
        }

        .form-group.fl_icon .form-input {
            padding-left: 70px;
        }

        .form-group textarea.form-input {
            height: 150px;
        }

        .row{
            display: grid;
            grid-template-columns: 50% 50%;
            grid-template-rows: 100%;
            grid-column-gap: 5px;
        }



        .btn {
            width: 180px;
            height: 45px;
            cursor: pointer;
            background: transparent;
            border: 1px solid gray;
            outline: none;
            transition: 1s ease-in-out;
        }

        svg {
            position: absolute;
            left: 0;
            top: 0;
            fill: none;
            stroke: #fff;
            stroke-dasharray: 150 480;
            stroke-dashoffset: 150;
            transition: 1s ease-in-out;
        }

        .btn:hover {
            transition: 1s ease-in-out;
            background: darkgray;
        }

        .btn:hover svg {
            stroke-dashoffset: -480;
        }

        .btn span {
            color: white;
            font-size: 18px;
            font-weight: 100;
        }
    </style>


</head>
<body>

<%
    String safe = ESAPI.encoder().encodeForHTML( "<script>alert(\\\"hello there\\\") </script>" );
    //String safe = "<script>alert(\"hello there\") </script>";
    Object message = request.getAttribute("message");
    Object hi = request.getAttribute("message");

    String name = request.getAttribute("name").toString();
    String email = request.getAttribute("email").toString();
    String kommentar = ESAPI.encoder().encodeForHTML(request.getAttribute("kommentar").toString());
    String anz_kommentare = ESAPI.encoder().encodeForHTML(request.getAttribute("length").toString());

    Object kommentar2 = request.getAttribute("kommentar");
    System.out.println("hier gehts los");
    System.out.println(kommentar2);


%>



<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<div class="container">
    <div class="be-comment-block">
        <h1 class="comments-title">Comments (<%=anz_kommentare%>)</h1>
        <c:forEach items="<%=kommentar%>" var="comment">
            <div class="be-comment">
                <div class="be-img-comment">
                    <a href="blog-detail-2.html">
                        <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="" class="be-ava-comment">
                    </a>
                </div>
                <div class="be-comment-content">

                    <span class="be-comment-name">
                        <a href="blog-detail-2.html">${nothinghereyet}</a>
                    </span>
                    <span class="be-comment-time">
                        <i class="fa fa-clock-o"></i>
                        May 27, 2015 at 3:14am
                    </span>

                    <p class="be-comment-text">

                            ${comment}
                    </p>
                </div>
            </div>
        </c:forEach>


        <form class="form-block" method="post" action="/AddCommentServlet">
            <div class="row">
                <div class="col-xs-12 col-sm-6">
                    <div class="form-group fl_icon">
                        <div class="icon"><i class="fa fa-user"></i></div>
                        <input class="form-input" type="text" placeholder="Your name" name="name">
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 fl_icon">
                    <div class="form-group fl_icon">
                        <div class="icon"><i class="fa fa-envelope-o"></i></div>
                        <input class="form-input" type="text" placeholder="Your email" name="mail_addr">
                    </div>
                </div>
            </div>
            <div class="col-xs-12">
                <div class="form-group">
                    <textarea class="form-input" required="" name="comment" placeholder="Your text" ></textarea>
                </div>
            </div>
            <button class="btn btn-primary pull-right">submit</button>

        </form>
    </div>
</div>



</body>
</html>
