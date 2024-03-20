    document.addEventListener('DOMContentLoaded', function () {
    const reviewForm = document.getElementById('reviewForm');

    reviewForm.addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the default form submission

    const userId = document.getElementById('userId').value;
    const storeId = document.getElementById('storeId').value;
    const comment = document.getElementById('content').value;
    const rating = document.querySelector('input[name="rating"]:checked').value;

    const reviewData = {
    userId: userId,
    comment: comment,
    rating: parseInt(rating), // 별점 점수로 변환
};

        fetch(`/store/${storeId}/review`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to submit review. Status: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log('Review submitted successfully:', data);
                window.location.href = `/store/${storeId}/reviews`; // Redirect to the reviews page
            })
            .catch(error => {
                console.error('Error:', error);
            });

    });
});
