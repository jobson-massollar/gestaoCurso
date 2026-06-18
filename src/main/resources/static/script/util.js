function main() {
    document.body.addEventListener('htmx:beforeSwap', function(evt) {
        if(evt.detail.xhr.status === 404){
            // alert the user when a 404 occurs (maybe use a nicer mechanism than alert())
            alert("Error: Could Not Find Resource");
        }
        // else if(evt.detail.xhr.status === 422){
        //     // allow 422 responses to swap as we are using this as a signal that
        //     // a form was submitted with bad data and want to rerender with the
        //     // errors
        //     //
        //     // set isError to false to avoid error logging in console
        //     evt.detail.shouldSwap = true;
        //     evt.detail.isError = false;
        // }
        // else if(evt.detail.xhr.status === 418){
        //     // if the response code 418 (I'm a teapot) is returned, retarget the
        //     // content of the response to the element with the id `teapot`
        //     evt.detail.shouldSwap = true;
        //     evt.detail.target = htmx.find("#teapot");
        // }
    });


    document.body.addEventListener('htmx:confirm', function(evt) {
        // 1. The requirement to show the sweet alert is that the element has a confirm-with-sweet-alert
        //    attribute on it, if it doesn't we can return early and let the default behavior happen
        if (!evt.detail.target.hasAttribute('confirm-with-sweet')) return

        // 2. Get the question from the attribute
        const question = evt.detail.target.getAttribute('confirm-with-sweet');

        // 3. Prevent the default behavior (this will prevent the request from being issued)
        evt.preventDefault();

        // 4. Show the sweet alert
        Swal.fire({
            title: "Are you sure?",
            text: question || "Are you sure you want to continue?",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((confirmed) => {
            if (confirmed) {
                // 5. If the user confirms, we can manually issue the request
                evt.detail.issueRequest(true); // true to skip the built-in window.confirm()
            }
        });
    });
}