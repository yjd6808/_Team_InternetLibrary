function likebox_OnMouseOver() {
    $("#like-img").css({
        'filter': 'brightness(0) invert(1)',
        'transition' : 'all 0.3s linear'
    });
}

function likebox_OnMouseLeave() {
    $("#like-img").css({
        'filter': 'brightness(1) invert(0)',
        'transition' : 'none'
    });
}