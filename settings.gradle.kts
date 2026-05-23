rootProject.name = "clinicflow"

include(
    ":app",
    ":shared:web",
    ":modules:auth",
    ":modules:tenant",
    ":modules:catalog",
    ":modules:appointment",
    ":modules:crm",
    ":modules:notification",
    ":modules:billing",
    ":modules:analytics",
)
