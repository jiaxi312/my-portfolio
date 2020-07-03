/** A program that handles the image gallary */
const numImages = 8;    // The number of images
let imageIdx = 0;       // The index of current image being shown

/** Displays the previous image */
function showPrevImage() {
  // Decrease the index by 1, round up to the last image if the index become -1
  imageIdx = (imageIdx - 1 + numImages) % numImages;
  showImageWithCurrentIdx();
}

/** Displays the next image */
function showNextImage() {
  /* Increase the index by 1, round up to the first image if the index is
     larger than the number of images */
  imageIdx = (imageIdx + 1) % numImages;
  showImageWithCurrentIdx();
}

/** Displays the image with the current index */
function showImageWithCurrentIdx() {
  // Change the header to show the current index of the image being shown
  const headerElement = document.getElementById('petsImage')
                        .getElementsByTagName('h1')[0];
  headerElement.innerText = `Pets Image (${imageIdx + 1}/${numImages})`;

  // Create the img element and the link to the image
  const imagePath = `./images/pets${imageIdx}.jpg`;
  const imageSrcElement = document.createElement('img');
  imageSrcElement.setAttribute('src', imagePath);
  const imageLinkElement = document.createElement('a');
  imageLinkElement.setAttribute('href', imagePath);
  imageLinkElement.appendChild(imageSrcElement);

  // Display the selected image to the webpage
  const imageContainer = document.getElementById('image-thumbnails');
  imageContainer.innerText = '';
  imageContainer.appendChild(imageLinkElement);
}