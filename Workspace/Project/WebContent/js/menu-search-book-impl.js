// 도서 검색 페이지에서만 동작해야하는 도서 검색 기능 추가 구현

$(() =>  search(1, '', SEARCh_OPTION_NONE, 10));

function selectBookTableRow_OnClick(u_id, name, writerName, publisherName) {
	location.href=`menu-search-info.jsp?book_uid=${u_id}`;
}