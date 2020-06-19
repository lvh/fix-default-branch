# fix-default-branch

**☢ Experimental ☢**

No more `master`. This will (destructively!) rename `master` to something else
(default: `trunk`), and change the GitHub default branch for the repo.

## Caveats

This does *not* work on "user pages" GitHub Pages repositories. "User pages" are
the kind that are named `<USER>/<USER>.github.io` and serve up GitHub Pages
content. GitHub mandates that these are called `master`. GitHub will need to fix
that one.

Existing users will get the somewhat unhelpful error message when they attempt
to pull from or push to master after running this tool:

```
fatal: couldn't find remote ref master
```

GitHub would need something like a "rename branch" feature (similar to how
"rename repo" exists) to fix this. Users should be OK to just run
`fix-default-branch`, but of course that assumes they have that installed and
recognize the error message.

## Installation

Download from https://github.com/lvh/fix-default-branch/releases.

## How to use

Typing `fix-default-branch` in a Git repo will:

* Rename `master` to `trunk` (unless you set something else with `--new-branch`)
* Attempt to push `trunk` to every push remote
* For every push remote on GitHub, attempt to patch the default branch to the new branch

## Details

This tool will never use force, implicitly (e.g. `git branch -D`, `git branch
-M`) or explicitly (`--force`), for deleting or renaming branches.

This tool talks to GitHub by using the OAuth tokens for [hub][hub] or [gh][gh].
It assumes one of those two is installed.

[hub]: https://github.com/github/hub
[gh]: https://github.com/cli/cli

## Development

Run the project directly:

    $ clj -m io.lvh.fix-default-branch

Run the project's tests:

    $ clj -A:test:runner

## Acknowledgements

* Inspired by [this Gist from @mechanicalgirl](https://gist.github.com/mechanicalgirl/46cb147f30ced94115f91268885eda99)

## License

Copyright © Laurens Van Houtven (@lvh)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
