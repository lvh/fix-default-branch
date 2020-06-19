# fix-default-branch

**☢ Experimental ☢**

No more `master`. This will (destructively!) rename `master` to something else
(default: `trunk`), and change the GitHub default branch for the repo.

[hub]: https://github.com/github/hub
[gh]: https://github.com/cli/cli

Note that this does *not* work on "user pages" GitHub Pages repositories. "User
pages" are the kind that are named `<USER>/<USER>.github.io`. GitHub will need
to fix that one.

This is intended as a tool to make changing your default branch easier. Please
do not use it to harass people.

## Installation

Download from https://github.com/lvh/fix-default-branch.

## How to use

Typing `fix-default-branch` in a Git repo will:

* Rename `master` to `trunk` (unless you set something else with `--new-branch`)
* Attempt to push `trunk` to every remote (it is not this tool's job to figure out access control)
* For every GitHub remote, attempt to patch the default branch to the new branch

## Details

This will never use force, implicitly (e.g. `git branch -D`, `git branch -M`) or
explicitly (`--force`), for deleting or renaming branches.


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
