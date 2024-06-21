flowchart LR
%% Nodes
    Start("fas:fa-mobile-screen Screen")
    Ok0("fas:fa-user Ok 0ch fas:fa-keyboard")
    Min("fas:fa-user MinCh fas:fa-exclamation-circle")
    Ok2("fas:fa-user Ok 2ch fas:fa-keyboard")
    Inval2("fas:fa-user Inval 2ch fas:fa-exclamation-circle")
    Ok3("fas:fa-user Ok 3ch fas:fa-keyboard")
    Inval3("fas:fa-user Inval 3ch fas:fa-exclamation-circle")
    Max("fas:fa-user MaxCh fas:fa-exclamation-circle")

%% Edge connections between nodes
    Start --fas:fa-hand-point-right icon--> Start
    Start --fas:fa-hand-point-right textfield--> Ok0

    Ok0 --fas:fa-hand-point-right icon--> Ok0
    Ok0 --fas:fa-delete-left--> Ok0
    Ok0 --fas:fa-keyboard valid ch--> Min
    Ok0 --fas:fa-keyboard inval ch--> Min

    Min --fas:fa-hand-point-right icon--> Ok0
    Min --fas:fa-delete-left valid ch--> Ok0
    Min --fas:fa-delete-left inval ch--> Ok0
    Min --fas:fa-keyboard valid ch--> Ok2
    Min --fas:fa-keyboard inval ch--> Inval2

    Ok2 --fas:fa-hand-point-right icon--> Ok0
    Ok2 --fas:fa-delete-left valid ch--> Min
    Ok2 --fas:fa-delete-left inval ch--> Min
    Ok2 --fas:fa-keyboard valid ch--> Ok3
    Ok2 --fas:fa-keyboard inval ch--> Inval3

    Inval2 --fas:fa-hand-point-right icon--> Ok0
    Inval2 --fas:fa-delete-left valid ch--> Min
    Inval2 --fas:fa-delete-left inval ch--> Min
    Inval2 --fas:fa-keyboard valid ch--> Inval3
    Inval2 --fas:fa-keyboard inval ch--> Inval3

    Ok3 --fas:fa-hand-point-right icon--> Ok0
    Ok3 --fas:fa-delete-left valid ch--> Ok2
    Ok3 --fas:fa-keyboard valid ch--> Max
    Ok3 --fas:fa-keyboard inval ch--> Max

    Inval3 --fas:fa-hand-point-right icon--> Ok0
    Inval3 --fas:fa-delete-left valid ch--> Inval2
    Inval3 --fas:fa-delete-left inval ch--> Ok2
    Inval3 --fas:fa-delete-left inval ch--> Inval2
    Inval3 --fas:fa-keyboard valid ch--> Max
    Inval3 --fas:fa-keyboard inval ch--> Max

    Max --fas:fa-hand-point-right icon--> Ok0
    Max --fas:fa-delete-left valid ch--> Ok3
    Max --fas:fa-delete-left valid ch--> Inval3
    Max --fas:fa-delete-left valid ch--> Max
    Max --fas:fa-delete-left inval ch--> Ok3
    Max --fas:fa-delete-left inval ch--> Inval3
    Max --fas:fa-delete-left inval ch--> Max
    Max --fas:fa-keyboard valid ch--> Max
    Max --fas:fa-keyboard inval ch--> Max

%% Individual node styling. Try the visual editor toolbar for easier styling!
    style Ok0 color:#FFFFFF, fill:#389400, stroke:#AA00FF
    style Ok2 color:#FFFFFF, fill:#389400, stroke:#AA00FF
    style Ok3 color:#FFFFFF, fill:#389400, stroke:#AA00FF
    style Min color:#FF0000, fill:#FFFFFF, stroke:#FF0000
    style Max color:#FF0000, fill:#FFFFFF, stroke:#FF0000
    style Inval2 color:#FF0000, fill:#FFFFFF, stroke:#FF0000
    style Inval3 color:#FF0000, fill:#FFFFFF, stroke:#FF0000
