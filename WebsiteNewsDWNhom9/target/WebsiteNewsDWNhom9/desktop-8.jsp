<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <link rel="icon" href="/favicon.ico" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <meta name="theme-color" content="#000000" />
  <title>Desktop - 8</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto%3A400%2C500%2C700"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro%3A400%2C500%2C600%2C700"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Montserrat%3A400%2C600"/>
  <link rel="stylesheet" href="./styles/desktop-8.css"/>
</head>
<body>
<div class="desktop-8-pTi">
  <div class="menu">
    <img class="farmers-1-1-UCp" src="./assets/farmers-1-1.png"/>
    <div class="search-box">
      <img class="search-icon" src="./assets/group.png"/>
      <p class="search-Q8Q">Search</p>
    </div>
    <div class="news-box">
      <img class="menu-news-img" src="./assets/group-tcx.png"/>
      <p class="news-kjz">News</p>
    </div>
  </div>
  <div class="main">
    <div class="left">
      <div class="auto-group-nhep-g9n">
        <p class="news-daily-PK6">News Daily</p>
        <p class="trending-RWg">Trending....</p>
      </div>
      <c:forEach items="${all}" var="news">
        <div class="news-item">
          <img class="news-img" src="./assets/rectangle-5-v88.png"/>
          <div class="news-text">
            <div class="container-news-item">
              <a class="news-title" href="/newsDetailController?id=${news.id}">${news.title}</a>
              <p class="news-description">
              <span class="news-description-sub-0">
<%--              <br/>--%>
              </span>
                <span class="news-description-sub-1">
              &#34;${news.description}
              <br/>

              </span>
              </p>
<%--              <img class="save-icon" src="./assets/clarity-bookmark-solid-nUx.png"/>--%>
            </div>
            <p class="news-category">${news.category_name}</p>
            <p class="news-date">${news.full_date}</p>
          </div>
        </div>
      </c:forEach>
      <div class="news-item">
        <img class="news-img" src="./assets/rectangle-5-v88.png"/>
        <div class="news-text">
          <div class="container-news-item">
            <p class="news-title">Hay When You Need It</p>
            <p class="news-description">
              <span class="news-description-sub-0">
              
              <br/>
              
              </span>
              <span class="news-description-sub-1">
              &#34;Agriculture is the most healthful, most useful and most noble employment of man.
              <br/>
              
              </span>
            </p>
            <img class="save-icon" src="./assets/clarity-bookmark-solid-nUx.png"/>
          </div>
          <p class="news-category">Entertaiment</p>
          <p class="news-date">Thurday 09 2022</p>
        </div>
      </div>
      <div class="news-item">
        <img class="news-img" src="./assets/rectangle-5-git.png"/>
        <div class="news-text">
          <div class="container-news-item">
            <p class="news-title">Hay When You Need It</p>
            <p class="news-description">
              <span class="news-description-sub-0">
              
              <br/>
              
              </span>
              <span class="news-description-sub-1">
              &#34;Agriculture is the most healthful, most useful and most noble employment of man.
              <br/>
              
              </span>
            </p>
            <img class="save-icon" src="./assets/clarity-bookmark-solid-4J4.png"/>
          </div>
          <p class="news-category">
          George News
          <br/>
          
          </p>
          <p class="news-date">Thurday 09 2022</p>
        </div>
      </div>
      <div class="news-item">
        <img class="news-img" src="./assets/rectangle-5-uHA.png"/>
        <div class="news-text">
          <div class="container-news-item">
            <p class="news-title">Hay When You Need It</p>
            <p class="news-description">
              <span class="news-description-sub-0">
              
              <br/>
              
              </span>
              <span class="news-description-sub-1">
              &#34;Agriculture is the most healthful, most useful and most noble employment of man.
              <br/>
              
              </span>
            </p>
            <img class="save-icon" src="./assets/clarity-bookmark-solid-UNU.png"/>
          </div>
          <p class="news-category">
          News Washington
          <br/>
          
          </p>
          <p class="news-date">Thurday 09 2022</p>
        </div>
      </div>
      <div class="news-item">
        <img class="news-img" src="./assets/rectangle-5-t5n.png"/>
        <div class="news-text">
          <div class="container-news-item">
            <p class="news-title">Strawberry Ginger</p>
            <p class="news-description">
              <span class="news-description-sub-0">
              
              <br/>
              
              </span>
              <span class="news-description-sub-1">
              &#34;Agriculture is the most healthful, most useful and most noble employment of man.
              <br/>
              
              </span>
            </p>
            <img class="save-icon" src="./assets/clarity-bookmark-solid.png"/>
          </div>
          <p class="news-category">
          George Washington
          <br/>
          
          </p>
          <p class="news-date">Thurday 09 2022</p>
        </div>
      </div>
    </div>
    <div class="right">
      <div class="container-shadow">
        <c:set value="${top1}" var="top1News"/>
        <img class="new-news-img" src="${top1News.image_path}"/>
        <p class="new-news-title">${top1News.title}</p>
        <p class="new-news-description">${top1News.description}</p>
        <div class="container-date">
          <img class="date-icon" src="./assets/frame.png"/>
          <p class="new-news-date">${top1News.full_date}</p>
        </div>
        <div class="new-news">
          <p class="read-more">Read More</p>
          <img class="read-more-icon" src="./assets/frame-7Np.png"/>
        </div>


      </div>

    </div>
  </div>
</div>
</body>