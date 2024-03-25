document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('profile-completion-form');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = {
            address: document.getElementById('address').value,
            phoneNumber: document.getElementById('phone-number').value,
            role: document.getElementById('role').value,
            email: document.getElementById('email').value,
            userName: document.getElementById('userName').value
        };

        fetch('/profile-complete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }
                return response.json(); // Proceed as normal if not a redirect
            })
            .then(data => {
                console.log('Success:', data);
                // Redirect or show success message
                window.location.href = "/store";
            })
            .catch((error) => {
                console.error('Error:', error);
                // Handle or show error
            });
    });
});
