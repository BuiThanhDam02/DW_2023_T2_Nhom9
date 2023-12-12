<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <link rel="icon" href="/favicon.ico" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <meta name="theme-color" content="#000000" />
  <title>Desktop - 9</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto%3A400%2C700"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro%3A400%2C700"/>
  <link rel="stylesheet" href="./styles/desktop-9.css"/>
</head>
<body>
<div class="desktop-9-g7A">
  <div class="auto-group-6apq-bjv">
    <img class="farmers-1-1-Hck" src="./assets/farmers-1-1-9Xv.png"/>
    <div class="auto-group-pgxr-zXA">
      <img class="group-X1J" src="./assets/group-YhE.png"/>
      <p class="search-3kL">Search</p>
    </div>
    <div class="auto-group-nq9r-Aa4">
      <img class="group-6ic" src="./assets/group-AQ8.png"/>
      <p class="news-cS4">News</p>
    </div>
  </div>
  <div class="main">
    <c:set value="${newsDetail}" var="newsDetail"/>
    <p class="news-detail">Chi tiết tin tức</p>
    <div class="main-top">
      <img class="news-detail-img" src="D:\Data Warehouse\public\img\2023\12\8\maguire-jpeg-1702035232-170203-6378-2114-1702035874.jpg"/>
      <div class="news-detail-box">
        <p class="news-detail-title">${newsDetail.title}</p>
        <p class="news-detail-date">${newsDetail.full_date}</p>
        <p class="news-detail-category">${newsDetail.category_name}</p>
        <p class="news-detail-description">${newsDetail.description}</p>
      </div>
    </div>
    <p class="contents">Nội dung</p>
    <p class="news-detail-content">${newsDetail.content}</p>
  </div>
</div>
</body>