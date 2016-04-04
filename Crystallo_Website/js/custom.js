
// Makes the image modal take source image of event triggerer
$('#imageModal').on('show.bs.modal', function (event) {
  var img = $(event.relatedTarget); // Button that triggered the modal
  var imgSrc = img.attr('src'); // Extract src image

  var modal = $(this); // Getting the modal
  modal.find('.modal-img').attr('src', imgSrc);
});

// Activates tabs :
$('#nav-tabs a').click(function (e) {
  $(this).tab('show');
});

// Activate the scroll spy http://getbootstrap.com/javascript/#scrollspy
$('body').scrollspy({ target: '#navbar' });

// What to do when scrollspy activates something ?
$('#myScrollspy').on('activate.bs.scrollspy', function () {
  // do something…
})